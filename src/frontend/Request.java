package frontend;

public class Request extends Thread {
	
	int requestCategory;
	
	String requestParameters;
	
	int sequenceNumber;
	
	int requestID;
	
	

	int requestStatus;
	
	String[] resultFromReplica = new String[4];
	
	
	
	String resultOfRequest = "";
	
	
	public Request(int category, String parameters) {
		
		this.setRequestCategory(category);
		this.setRequestParameters(parameters);
		this.setRequestStatus(ConstantValue.WAIT_FOR_SEQUENCE_NUMBER);
		this.setSequenceNumber(ConstantValue.NON_SEQUENCE_NUMBER);
		
		
		resultFromReplica[1] = "empty1";
		resultFromReplica[2] = "empty2";
		resultFromReplica[3] = "empty3";

	}
	
	public String getFinalResult() {
		
		synchronized(this) {
			
			while(resultOfRequest == "" ) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			return resultOfRequest;
			
		}
		
		
	}
	
	

	@Override
	public void run() {
		
	}
	
	public String getResultOfRequest() {
		return resultOfRequest;
	}

	public void setResultOfRequest(String string) {
		
		synchronized(this) {
			this.resultOfRequest = string;
			this.notify();
		}
	}

	public int getRequestCategory() {
				
		return requestCategory;
	}

	public void setRequestCategory(int requestCategory) {
		this.requestCategory = requestCategory;
	}

	public String getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(String requestParameters) {
		this.requestParameters = requestParameters;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(int requestStatus) {
		this.requestStatus = requestStatus;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public String[] getResultFromReplica() {
		return resultFromReplica;
	}

	public void setResultFromReplica(String[] resultFromReplica) {
		this.resultFromReplica = resultFromReplica;
	}
	
	
}
