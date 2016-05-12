/**
 * 
 */
package ippoz.multilayer.monitor.communication;

import ippoz.multilayer.commons.support.AppLogger;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Tommy
 *
 */
public class CommunicationManager {

	private ServerSocket ssocket;
	private String ipAddress;
	private int ipPort;
	
	public CommunicationManager(String ipAddress, int inPort, int outPort) throws IOException {
		this.ipAddress = ipAddress;
		ipPort = outPort;
		ssocket = new ServerSocket(inPort);
	}
	
	public void send(Object toSend) throws IOException {
		LinkedList<Object> list = new LinkedList<Object>();
		list.add(toSend);
		send(list);
	}
	
	public void send(Object[] toSend) throws IOException {
		send(new LinkedList<Object>(Arrays.asList(toSend)));
	}
	
	public void send(Collection<Object> toSend) throws IOException {
		Socket socket = null;
		ObjectOutputStream objStream = null;
		try {
			while(socket == null) {
				try {
					socket = new Socket(ipAddress, ipPort);
				} catch (IOException e) {}
			}
			objStream = new ObjectOutputStream(socket.getOutputStream());
			for(Object obj : toSend){
				objStream.writeObject(obj);
				//AppLogger.logInfo(getClass(), "Sent: " + obj.toString());
			}
		} catch (UnknownHostException ex) {
			AppLogger.logException(getClass(), ex, "Unable to find host");
		} catch (IOException ex) {
			AppLogger.logException(getClass(), ex, "Unable to communicate");
		} finally {
			if(!socket.isClosed())
				socket.close();
		}
	}
	
	public LinkedList<Object> receive() throws IOException {
		LinkedList<Object> objList = null;
		Socket newSocket = null;
		ObjectInputStream oiStream = null;
		Object readed;
		try {
			newSocket = ssocket.accept();
			objList = new LinkedList<Object>();
			oiStream = new ObjectInputStream(newSocket.getInputStream());
			while((readed = oiStream.readObject()) != null){
				objList.add(readed);
				//AppLogger.logInfo(getClass(), "Received: " + readed.toString());
			}
		} catch (EOFException ex){
			//AppLogger.logInfo(getClass(), "No such elements in stream");
		} catch (IOException ex) {
			AppLogger.logException(getClass(), ex, "Unable to get messages");
		} catch (ClassNotFoundException ex) {
			AppLogger.logException(getClass(), ex, "Wrong object on the flow");
		} finally {
			if(oiStream != null)
				oiStream.close();
			if(newSocket != null && !newSocket.isClosed())
				newSocket.close();
		}
		return objList;
	}
	
	public void waitFor(MessageType message) throws IOException {
		boolean found = false;
		while(!found) {
			for(Object obj : receive()){
				if(obj instanceof MessageType && ((MessageType)obj).equals(message)){
					found = true;
					break;
				}
			}
		}
	}
	
	public void waitForConfirm() throws IOException {
		//AppLogger.logInfo(getClass(), "Waiting for confirm");
		waitFor(MessageType.OK);
	}
	
	public void flush() throws IOException{
		ssocket.close();
	}

	public boolean isAlive() {
		return !ssocket.isClosed();
	}

	public String getIpAddress() {
		return ipAddress;
	}
	
}
