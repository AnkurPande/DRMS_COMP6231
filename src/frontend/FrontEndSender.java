package frontend;

import java.util.Timer;
import java.util.TimerTask;

import udp.Multicaster;

public class FrontEndSender extends Thread {
	
	FrontEnd frontend;
	
	public FrontEndSender(FrontEnd frontend) {
		this.frontend = frontend;
	}
	
	@Override
	public void run() {
		
		while(true) {
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while(!frontend.getHoldingQueue().isEmpty()) {
				
				Request currentRequest = frontend.getHoldingQueue().poll();

				


				frontend.setRequestID(frontend.getRequestID() + 1);
				
				currentRequest.setRequestID(frontend.getRequestID());
				
				frontend.getUnSequencedRequests().put(currentRequest.requestID, currentRequest);

				
				
				Multicaster multicaster = new Multicaster(4001, "235.1.10.1");
				

				
				String requestData = ConstantValue.SEND_REQUEST + "," +currentRequest.getRequestID() + "," + currentRequest.getRequestCategory()+ ","+ currentRequest.getRequestParameters();
				

				multicaster.sendMessage (requestData);
					
			
				currentRequest.setRequestStatus(ConstantValue.WAIT_FOR_RESULT);
				
				Timer timer = new Timer();
				timer.schedule(new SequenceNumberChecker(currentRequest,frontend), 1000000);
				
				
			}
		}
	}
	
	public class SequenceNumberChecker extends TimerTask {
		
		Request request;
		FrontEnd frontend;
		public SequenceNumberChecker(Request request, FrontEnd frontend) {
			this.request = request;
			this.frontend = frontend;
		}
		
		@Override
		public void run() {
			if(request.getSequenceNumber() == ConstantValue.NON_SEQUENCE_NUMBER) {
				frontend.getUnSequencedRequests().remove(request.requestID, request);
				frontend.getHoldingQueue().add(request);
				
			}
			
		}
		
	}

}
