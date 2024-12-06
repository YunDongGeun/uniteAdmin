package org.example.mvc.view;


import org.example.global.Protocol;
import org.example.mvc.packet.LoginPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
public class LoginView {
    private Socket clientSocket = null;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Protocol protocol;
    private LoginPacket idPacket;
    private LoginPacket pwdPacket;
    Scanner sc = new Scanner(System.in);

    public void printSignIn() {
        try {
            System.out.println("=== 로그인 ===");
            System.out.print("아이디: ");
            String id = sc.next();
            System.out.print("비밀번호: ");
            String pwd = sc.next();

            idPacket = new LoginPacket(id, "id");
            System.out.println(idPacket.toString());
            pwdPacket = new LoginPacket(pwd, "pwd");
            System.out.println(pwdPacket.toString());

            // id 요청 전송
            oos.writeObject(idPacket);
            System.out.println("write?");
            protocol = (LoginPacket) ois.readObject();

            // pwd 요청 전송
            oos.writeObject(pwdPacket);
            protocol = (LoginPacket) ois.readObject();

            if (protocol.getType() == Protocol.TYPE_RESPONSE &&
                    protocol.getCode() == Protocol.CODE_SUCCESS) {
                System.out.println("login success");
            }
        } catch (Exception e) {
            System.out.println("로그인 오류: " + e.getMessage());
        }
    }
//    public void printSignUp() {
//        try {
//            connectToServer();
//            System.out.println("=== 회원가입 ===");
//            System.out.print("아이디: ");
//            String id = sc.next();
//            System.out.print("비밀번호: ");
//            String pwd = sc.next();
//            // 회원가입 요청 전송
//            protocol = new LoginMessage();
//            protocol.setType(LoginMessage.SIGNUP_REQUEST);
//            protocol.setId(id);
//            protocol.setPwd(pwd);
//            oos.writeObject(protocol);
//            // 결과 수신
//            protocol = (LoginMessage) ois.readObject();
//            System.out.println(protocol.getMessage());
//        } catch (Exception e) {
//            System.out.println("회원가입 오류: " + e.getMessage());
//        } finally {
//            closeConnection();
//        }
//    }

    private void closeConnection() {
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("연결 종료 오류: " + e.getMessage());
        }
    }
}
