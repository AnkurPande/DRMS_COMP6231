package model;
import java.lang.Math.*;
import java.net.*;
import java.io.*;

/*
  Class: DatagramWrapper
  Author: Jay Elrod
  Description: This class is used to represent a packet of
      custom type. It's fields will typically represent parsed
      datagram information. DatagramWrappers can be transformed
      into DatagramPackets and vice versa via DatagramWrapper
      methods. This is necessary because a DatagramSocket only
      accepts DatagramPackets and the class cannot be extended.
 */

public class DatagramWrapper {
    private long start;
    private long end;
    private byte[] payload;
    InetAddress address;
    int port = 1111;

    //default constructor
    public DatagramWrapper() {
	this.setStart(-1);
	this.setEnd(0);
	this.port = 0;
    }	

    //data packet
    public DatagramWrapper (long start, long end, byte[] payload, InetAddress address, int port) {
	this.setStart(start);
	this.setEnd(end);
	this.setPayload(payload);
	this.address = address;
	this.port = port;
    }

    //ACK packet
    public DatagramWrapper (long start, long end, InetAddress address, int port) {
	this.setStart(start);
	this.setEnd(end);
	this.setPayload(new byte[0]);
	this.address = address;
	this.port = port;
    }

    //returns Protogram in datagram format
    public DatagramPacket asDatagram() {
	byte[] bytes = new byte[getPayload().length+32];
	int i;
	for ( i=0; i<Long.toString(this.getStart()).length(); i++ )
	    bytes[i] = (byte)(Long.toString(this.getStart()).charAt(i));
	while ( i<16 ) { bytes[i] = (byte)(' '); i++; }
	for ( i=i; i<(Long.toString(this.getEnd()).length())+16; i++ )
	    bytes[i] = (byte)(Long.toString(this.getEnd()).charAt(i-16));
	while ( i<32 ) { bytes[i] = (byte)(' '); i++; }
	for ( int j=0; j<getPayload().length; j++) {
	    bytes[i] = getPayload()[j];
	    i++;
	}
	return new DatagramPacket(bytes, getPayload().length+32, address, port);
    }

    //writes the payload of the protogram to the specified output stream
    public void writePayload(OutputStream destination) {
	try {
	    destination.write(getPayload());
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    //comparison to tell if two protograms are identical
    public boolean equals(DatagramWrapper anotherProtogram) {
	if ( this.getStart() != anotherProtogram.getStart() ||
	     this.getEnd() != anotherProtogram.getEnd() ) return false;
	for ( int i=0; i<Math.min(this.getPayload().length, anotherProtogram.getPayload().length); i++ )
	    if ( this.getPayload()[i] != anotherProtogram.getPayload()[i] ) return false;
	return true;
    }

    //returns datagram in protogram format
    public static DatagramWrapper fromDatagram(DatagramPacket pkt) {
	byte[] bytes;
	long newStart = Long.parseLong((new String(pkt.getData())).substring(0,15).trim());
	long newEnd = Long.parseLong((new String(pkt.getData())).substring(16,31).trim());
	try {
	    bytes = new byte[(int)(newEnd - newStart)];
	} catch ( Exception e ) {
	    bytes = new byte[0];
	}
	for (int i=0; i<bytes.length; i++)
	    bytes[i] = pkt.getData()[i+32];
	
	return new DatagramWrapper (newStart, newEnd, bytes, pkt.getAddress(), pkt.getPort());				    
    }

    // Getters and setters....
	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

}