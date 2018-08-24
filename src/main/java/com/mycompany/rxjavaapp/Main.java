package com.mycompany.rxjavaapp;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Main {

    static String filePath = "/home/luan/text.txt";

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {

        Observable<Object> observable = Observable.create((emmiter) -> {
            try {
                PrintWriter writer = new PrintWriter(filePath, "UTF-8");
                for (int i = 0; i < 10; i++) {
                    writer.println("line: " + i);
                    writer.flush();
                    emmiter.onNext("next");
                }
                writer.close();
            } catch (Exception e) {
                emmiter.onError(e);
            }

            emmiter.onComplete();
        }).observeOn(Schedulers.newThread());

        createThred(1, observable);
        createThred(2, observable);
        createThred(3, observable);

        Thread.sleep(5000);

    }

    public static void createThred(int leitor, Observable<Object> observable) {

        try {
            Scanner scan = new Scanner(new File(filePath));
            observable.subscribe(
                    (next) -> {
                        if (scan.hasNext()) {
                            System.out.println("leitor " + leitor + ": " + scan.nextLine());
                        }
                    }, (error) -> {
                        System.err.println(error.getMessage());
                    }, () -> {
                        System.out.println("observable: " + leitor + " Complete");
                    });
        } catch (Exception e) {
        }

    }
}
