package org.example.mvc.view;

import org.example.global.Protocol;
import org.example.mvc.packet.LoginPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
public class LoginView {
    private static final String SERVER_ADDRESS = "121.150.205.210"; // 서버 주소
    private static final int SERVER_PORT = 8888; // 서버 포트

    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private final Scanner scanner;

    public LoginView() {
        this.scanner = new Scanner(System.in);
    }

    private void connectToServer() throws IOException {
        try {
            clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new IOException("서버 연결 실패: " + e.getMessage());
        }
    }

    public void printSignIn() {
        try {
            connectToServer(); // 서버 연결 먼저 수행

            System.out.println("=== 로그인 ===");
            System.out.print("아이디: ");
            String id = scanner.next();
            System.out.print("비밀번호: ");
            String pwd = scanner.next();

            // ID 검증
            LoginPacket idPacket = new LoginPacket(id, "id");
            System.out.println("전송할 ID 패킷: " + idPacket);
            oos.writeObject(idPacket.getPacket()); // getPacket()으로 바이트 배열 전송
            oos.flush(); // 버퍼 비우기

            Protocol response = (Protocol) ois.readObject();
            if (response.getCode() != Protocol.CODE_SUCCESS) {
                System.out.println("ID 검증 실패");
                return;
            }

            // 비밀번호 검증
            LoginPacket pwdPacket = new LoginPacket(pwd, "pwd");
            System.out.println("전송할 Password 패킷: " + pwdPacket);
            oos.writeObject(pwdPacket.getPacket());
            oos.flush();

            response = (Protocol) ois.readObject();
            if (response.getType() == Protocol.TYPE_RESPONSE &&
                    response.getCode() == Protocol.CODE_SUCCESS) {
                System.out.println("로그인 성공!");
            } else {
                System.out.println("로그인 실패: 잘못된 비밀번호");
            }

        } catch (IOException e) {
            System.out.println("통신 오류: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("프로토콜 오류: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            if (oos != null) {
                oos.flush();
                oos.close();
            }
            if (ois != null) ois.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            System.err.println("연결 종료 오류: " + e.getMessage());
        }
    }
}