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
            System.out.println("전송할 ID 패킷: " + idPacket.toString());
            byte[] idData = idPacket.getPacket();
            out.writeInt(idData.length);
            out.write(idData);
            out.flush();

            // 서버 응답 읽기
            int responseLength = in.readInt();
            if (responseLength <= 0 || responseLength > Protocol.LEN_MAX) {
                throw new IOException("잘못된 응답 길이: " + responseLength);
            }
            System.out.println("응답 길이: " + responseLength);

            byte[] responseData = new byte[responseLength];
            in.readFully(responseData);
            System.out.println("서버로부터 ID 응답 수신 완료");

            // PWD 전송
            LoginPacket pwdPacket = new LoginPacket(pwd, "pwd");
            System.out.println("전송할 PWD 패킷: " + pwdPacket.toString());
            byte[] pwdData = pwdPacket.getPacket();
            out.writeInt(pwdData.length);
            out.write(pwdData);
            out.flush();

            // 서버 응답 읽기
            responseLength = in.readInt();
            if (responseLength <= 0 || responseLength > Protocol.LEN_MAX) {
                throw new IOException("잘못된 응답 길이: " + responseLength);
            }
            System.out.println("응답 길이: " + responseLength);

            responseData = new byte[responseLength];
            in.readFully(responseData);
            System.out.println("서버로부터 PWD 응답 수신 완료");

            // 응답 처리
            Protocol response = new Protocol();
            // TODO: responseData를 Protocol 객체로 변환하는 로직 필요

            if (response.getType() == Protocol.TYPE_RESPONSE &&
                    response.getCode() == Protocol.CODE_SUCCESS) {
                System.out.println("로그인 성공");
            } else {
                System.out.println("로그인 실패");
            }
        } catch (IOException e) {
            System.out.println("통신 오류: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("기타 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }
}