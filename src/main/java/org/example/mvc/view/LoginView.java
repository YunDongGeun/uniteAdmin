package org.example.mvc.view;

import lombok.Getter;
import org.example.global.Protocol;
import org.example.mvc.packet.LoginPacket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class LoginView {
    @Getter
    private final Socket clientSocket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Scanner scanner;

    public LoginView(Socket socket, DataInputStream in, DataOutputStream out) {
        this.clientSocket = socket;
        this.in = in;
        this.out = out;
        this.scanner = new Scanner(System.in);
    }

    public void printSignIn() {
        try {
            System.out.println("=== 로그인 ===");
            System.out.print("아이디: ");
            String id = scanner.next();
            System.out.print("비밀번호: ");
            String pwd = scanner.next();

            // ID 전송
            LoginPacket idPacket = new LoginPacket(id, "id");
            System.out.println(idPacket.toString());
            byte[] idData = idPacket.getPacket();
            out.writeInt(idData.length);  // 먼저 데이터 길이 전송
            out.write(idData);            // 실제 데이터 전송
            out.flush();

            // 서버 응답 읽기
            int responseLength = in.readInt();
            byte[] responseData = new byte[responseLength];
            in.readFully(responseData);
            Protocol response = new Protocol();  // 응답 처리 로직 필요

            // PWD 전송
            LoginPacket pwdPacket = new LoginPacket(pwd, "pwd");
            System.out.println(pwdPacket.toString());
            byte[] pwdData = pwdPacket.getPacket();
            out.writeInt(pwdData.length);
            out.write(pwdData);
            out.flush();

            // 서버 응답 읽기
            responseLength = in.readInt();
            responseData = new byte[responseLength];
            in.readFully(responseData);
            response = new Protocol();  // 응답 처리 로직 필요

            if (response.getType() == Protocol.TYPE_RESPONSE &&
                    response.getCode() == Protocol.CODE_SUCCESS) {
                System.out.println("로그인 성공");
            } else {
                System.out.println("로그인 실패");
            }
        } catch (Exception e) {
            System.out.println("로그인 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
}