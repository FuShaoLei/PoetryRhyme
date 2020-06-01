package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

public class SocketServer {
	private static String poem_url="https://v1.jinrishici.com/all.json";
	private static ServerSocket serverSocket;
	private static final int SERVER_PORT=4321;
	private static String problem="���֪_��ˮ ��������ѹ�Ǻ� ";
	private static String key="��";
	private static ArrayList<SocketClass> socketList=new ArrayList<SocketClass>();
	private static  int classId=1;//��һ��ʼ
	private static Map<Integer,Boolean> firstMap=new HashMap<Integer, Boolean>();//��¼���͹���
	private boolean flag=true;
	
	public static void main(String[] args) {
		initServer();
	}
	public static void initServer() {
		try {
			serverSocket=new ServerSocket(SERVER_PORT);
			System.out.println("��������������˿ں���: "+SERVER_PORT);
			System.out.println("==========�ȴ��ͻ���������==========");
			sendProblemThread();
			while(true) {
				Socket socket=serverSocket.accept();
				System.out.println("�ͻ��������ӣ��ͻ��˵�IP��ַ��:"+socket.getInetAddress());
				startAction(socket);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * �����Ķ�ȡ�ͻ��˷�������Ϣ
	 * ���ж�һ��socket�Ƿ񻹴�����
	 * @author sorryfu
	 *
	 */
	public static class readThread extends Thread{
		public SocketClass socketClass;
		public BufferedReader reader;
		public BufferedWriter writer;
		public readThread(SocketClass socketClass) {
			this.socketClass=socketClass;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {

				System.out.println("idΪ"+socketClass.getId()+"��readThread������");
				while(true) {
					if(socketClass.getFriendId()!=0) {//������0��ʾ�Ѿ�ƥ������
						System.out.println("idΪ"+socketClass.getId()+"ƥ������");
						//�Լ���������
						InputStream inputStream=socketClass.getSocket().getInputStream();
						reader=new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
						//�Է��������
						OutputStream outputStream = null;
						for(int i=0;i<socketList.size();i++) {
							if(socketList.get(i).getFriendId()==socketClass.getUserId()) {
								outputStream=socketList.get(i).getSocket().getOutputStream();
							}
						}
						writer=new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));
						if(reader.ready()) {
						System.out.println("idΪ"+socketClass.getId()+"��reader׼�����ˡ�����");
						String comeData=reader.readLine();
						JSONObject msgJson=new JSONObject(comeData);//����android�˴�������json����
						if(msgJson.has("game_my_anwser")) {
							System.out.println("user idΪ"+socketClass.getUserId()+"�������Ĵ��ǣ�"+msgJson.getString("game_my_anwser"));
							JSONObject other_anwser=new JSONObject();
							other_anwser.put("game_other_anwser", msgJson.getString("game_my_anwser"));
							writer.write(other_anwser.toString() +"\n");
							writer.flush();
							
							firstMap.put(socketClass.getId(), true);
							sysoutMap();
						}
						
						
					}
						
					
					}
					Thread.sleep(2000);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}
	//����������socket������������
	public static void startAction(final Socket socket) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					InputStream inputStream=socket.getInputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
					
					while(true) {
						
						String message=reader.readLine();
						JSONObject jsonObject=new JSONObject(message);
						
						
						if(jsonObject.getString("game_userId")!=null) {
						int userId=Integer.parseInt(jsonObject.getString("game_userId"));
						
						System.out.println("��ȡ���ͻ��˵�id�ǣ�"+userId);
						
						firstMap.put(classId, false);//��
						SocketClass freshSocket=new SocketClass(classId,userId, socket);
						socketList.add(freshSocket);
						
						readThread rThread=new readThread(freshSocket);//�������̷߳���������Ϣ
						rThread.start();
						
						match();//��������while��true��ѭ������Ի�һֱ�ȴ�ƥ��
						firstMessage();//��һ��ƥ����֮���أ��Ȼ��෢��id�Ը���ui
						
						classId++;
						}else {
							System.out.println("����ͻ���ɶҲû������");
						}
					} 
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}.start();
	}
	public static void sysoutMap() {
		for(Map.Entry<Integer, Boolean> entry : firstMap.entrySet()) {
			System.out.println("key = "+entry.getKey()+", value="+entry.getValue());
		}
	}
	
	/**
	 * ��һ��ƥ����֮�󣬸�˫�����ͶԷ���id
	 */
	public static void firstMessage(){
		System.out.println("����ƥ����˫��������Ϣ");
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					if(socketList.size()>=2) {
						for(int i=0;i<socketList.size();i++) {
						if(socketList.get(i).getFriendId()!=0) {
							for(int j=i+1;j<socketList.size();j++) {
								if(socketList.get(j).getFriendId()!=0) {
									if(socketList.get(i).getFriendId()==socketList.get(j).getUserId()&&socketList.get(j).getFriendId()==socketList.get(i).getUserId()) {
										System.out.println("����ǰ��map�������ģ�");
										sysoutMap();
										//���˫˫��Ϊfriendid�Ļ�������Է���һ��id
										//1.��ȡ���Դ
										OutputStream output1=socketList.get(i).getSocket().getOutputStream();
										OutputStream output2=socketList.get(j).getSocket().getOutputStream();
										BufferedWriter writer1=new BufferedWriter(new OutputStreamWriter(output1,"utf-8"));
										BufferedWriter writer2=new BufferedWriter(new OutputStreamWriter(output2,"utf-8"));
										//2.��װ
//										String b=socketList.get(i).getFriendId()+"\n";
//										output1.write(b.getBytes("utf-8"));
//										String a=socketList.get(j).getFriendId()+"\n";
//										output2.write(a.getBytes("utf-8"));
										JSONObject jsonObject1=new JSONObject();
										JSONObject jsonObject2=new JSONObject();
										jsonObject1.put("game_friendId", socketList.get(i).getFriendId());
										jsonObject2.put("game_friendId", socketList.get(j).getFriendId());
										writer1.write(jsonObject1.toString()+"\n");
										writer2.write(jsonObject2.toString()+"\n");
										
										writer1.flush();
										writer2.flush();
										
										Thread.sleep(200);
										//�������÷���˸��ͻ��˷�����Ŀ
										firstMap.put(socketList.get(i).getId(), true);
										firstMap.put(socketList.get(j).getId(), true);
										
										System.out.println("�Ѹ���ƥ���˫�����ͶԷ���id");
										sysoutMap();
										
									}
								}
							}
						}
						}
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	  /**
	   * ��ȡ��Ŀ�ʹ�
	   * @param requestUrl
	   * @return
	   */
	  public static String getXpath(String requestUrl){
	        String res="";
	        String hh="";
	        JSONObject object = null;
	        StringBuffer buffer = new StringBuffer();
	        try{
	            URL url = new URL(requestUrl);
	            HttpURLConnection urlCon= (HttpURLConnection)url.openConnection();
	            if(200==urlCon.getResponseCode()){
	                InputStream is = urlCon.getInputStream();
	                InputStreamReader isr = new InputStreamReader(is,"utf-8");
	                BufferedReader br = new BufferedReader(isr);

	                String str = null;
	                while((str = br.readLine())!=null){
	                    buffer.append(str);
	                }
	                br.close();
	                isr.close();
	                is.close();
	                res = buffer.toString();
	                System.out.println("��һ�λ�ȡ�������ǣ�"+res);
	                hh=getPoetry(res);
	            }
	        }catch(IOException e){
	            e.printStackTrace();
	        }
	        return hh;
	    }
	  
	  private static String getPoetry(String json) {
		   String ge = "��";
	        //�п� �ƺ�û��ҪQAQ
	        if (json == null || json.trim().length() == 0) {
	            System.out.println("��ȻΪ��");
	        } else {
	            try {
	                JSONObject jsonObject = new JSONObject(json);
	                String mProblem = jsonObject.getString("content");
	                System.out.println("�õ���ʫ��Ϊ��"+mProblem);
	                ge=getProblem(mProblem);
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }

	        }
			return ge;

	    }
	  private static String getProblem(String allPoetry){
	        if(allPoetry.length()>0){
	            Random random=new Random();
	            char[] charArray=allPoetry.toCharArray();
	            while(true){
	                int randnum=random.nextInt(allPoetry.length());
	                String tt=String.valueOf(charArray[randnum]);
	                //��������ھͿ��Խ�����һ��
	                if(tt.equals("��")==false&&tt.equals("��")==false){

	                    System.out.println("��õ��������: "+randnum+"");
	                    System.out.println("���ժȡ������: "+charArray[randnum]+"");
	                    key=charArray[randnum]+"";
	                    System.out.println("my_key�𰸵���: "+key);
	                    charArray[randnum]='_';
	                    problem=new String(charArray);
	                    System.out.println("my_problem���ڣ� "+problem);
	                    
	                    break;
	                }

	            }
	        }else{
	            System.out.println("���󣡣���"+"δ��ȷ���ʫ�ʣ�");
	        }
	        
	        return problem;
	    }

	
	//ר����������Ŀ�ķ���
	public static void sendProblemThread() {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					//һֱѭ��
					while(true) {
						
						if(socketList.size()>=2) {
							for(int i=0;i<socketList.size();i++) {
							if(socketList.get(i).getFriendId()!=0&&firstMap.get(socketList.get(i).getId())==true) {
								for(int j=i+1;j<socketList.size();j++) {
									if(socketList.get(j).getFriendId()!=0&&firstMap.get(socketList.get(j).getId())==true) {
										if(socketList.get(i).getFriendId()==socketList.get(j).getUserId()&&socketList.get(j).getFriendId()==socketList.get(i).getUserId()) {
											//1.��ȡ�����
											OutputStream output1=socketList.get(i).getSocket().getOutputStream();
											OutputStream output2=socketList.get(j).getSocket().getOutputStream();
											
											BufferedWriter writer1=new BufferedWriter(new OutputStreamWriter(output1,"utf-8"));
											BufferedWriter writer2=new BufferedWriter(new OutputStreamWriter(output2,"utf-8"));
											
											//2.��װ��Ŀ�ʹ�
											System.out.println("��ȡ������Ŀ�ǣ�"+getXpath(poem_url)+"���ǣ�"+key);
											JSONObject jsonObject=new JSONObject();
											jsonObject.put("game_problem", problem);
											jsonObject.put("game_key", key);
											
											//3.д��
											writer1.write(jsonObject.toString()+"\n");
											writer2.write(jsonObject.toString()+"\n");
											writer1.flush();
											writer2.flush();
											
											//4.ÿ�η�������Ŀ������Ϊfalse������������Ϊtrue��ȫ������ɴ���󣩣��ŷ�����Ŀ
											firstMap.put(socketList.get(i).getId(), false);
											firstMap.put(socketList.get(j).getId(), false);
											
											System.out.println("����ĳ��ƥ���������Ŀ�ʹ�");
										}
									}
								}
							}
							}
						}
						Thread.sleep(500);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}.start();
		
	}
	
	
	//��ʼƥ��
	public static void match() {
		//intĬ��ֵ��0
		//���ڻ�2�ſ�ʼ������Ͳ���ƥ�俩
		if(socketList.size()>=2) {
			System.out.println("��ʼƥ��ǰ��״̬");
			for(SocketClass mSocket:socketList) {
				System.out.println(mSocket.toString());
			}
			for(int i=0;i<socketList.size();i++) {
				if(socketList.get(i).getFriendId()==0) {//friendId(int)�Ļ���Ĭ��ֵ��0�����ԣ��������0�Ļ�˵��û�У������ƥ�����
					for(int j=i+1;j<socketList.size();j++) {
						if(socketList.get(j).getFriendId()==0) {//����ڴ�ѭ���Ҳ��һ��friendIdΪ0�Ļ����ͽ�������ƥ��
							socketList.get(i).setFriendId(socketList.get(j).getUserId());
							socketList.get(j).setFriendId(socketList.get(i).getUserId());
						}
					}
					
				}
				
			}
			System.out.println("ƥ����״̬");
		for(SocketClass mSocket:socketList) {
			System.out.println(mSocket.toString());
		}
		}
		
	}
	
}
