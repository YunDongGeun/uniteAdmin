package org.example.mvc.view;

import lombok.Getter;
import org.example.global.Protocol;
import org.example.mvc.packet.LoginPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
public class LoginView {
    @Getter
    private final Socket clientSocket;
    private final ObjectInputStream ois;
    private final ObjectOutputStream oos;
    private final Scanner scanner;

    public LoginView(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.clientSocket = socket;
        this.ois = ois;
        this.oos = oos;
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
            oos.writeObject(idPacket.getPacket());
            oos.flush();

            Protocol response = (Protocol) ois.readObject();

            // PWD 전송
            LoginPacket pwdPacket = new LoginPacket(pwd, "pwd");
            System.out.println(pwdPacket.toString());
            oos.writeObject(pwdPacket.getPacket());
            oos.flush();

            response = (Protocol) ois.readObject();

            if (response.getType() == Protocol.TYPE_RESPONSE &&
                    response.getCode() == Protocol.CODE_SUCCESS) {
                System.out.println("로그인 성공");
            } else {
                System.out.println("로그인 실패");
            }
        } catch (Exception e) {
            System.out.println("로그인 오류: " + e.getMessage());
        }
    }

}