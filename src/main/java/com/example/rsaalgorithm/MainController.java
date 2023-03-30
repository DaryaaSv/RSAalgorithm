package com.example.rsaalgorithm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.io.*;
import java.math.BigInteger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {
    String result = "";
    String output;
    BigInteger gcd;
    String euc;

    @GetMapping("/")
    public String Main(Model model) {
        model.addAttribute("result", result);
        model.addAttribute("gcd", gcd);
        model.addAttribute("euc", euc);
        return "index";
    }
    @PostMapping("/")
    public String postMain(@RequestParam(required = false) String plain_text,
                           @RequestParam(required = false) String numberP,
                           @RequestParam(required = false) String numberQ,
                           @RequestParam(required = false) String numberN,
                           @RequestParam(required = false) String numberE,
                           @RequestParam(name = "to_file_system", required = false) String toFile,
                           @RequestParam(name = "file", required = false) MultipartFile file,
                           Model model
    ) throws Exception {

        if(!file.isEmpty()) {
            try {
                String filePath = file.getOriginalFilename();
                File fileInput = new File(filePath);
                if (!fileInput.exists()) {
                    throw new FileNotFoundException("File not found: " + filePath);
                }
                FileReader fileReader = new FileReader(fileInput);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                try {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] words = line.split(" ");
                        for (int i = 0; i < words.length; i++) {
                            if (words[i].equals("x")) {
                                for(int j = i + 2; j < words.length; j++) {
                                    plain_text += words[j];
                                    plain_text += " ";
                                }
                                break;
                            }
                        }
                    }
                } finally {
                    bufferedReader.close();
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RSA rsa = new RSA();
        if(!numberP.equals("")) {
            rsa = new RSA(new BigInteger(numberP), new BigInteger(numberQ));
            result = rsa.encrypt(plain_text);
            BigInteger[] publicKey = rsa.getPublicKey();
            output = "Encrypted plaintext x = " + result + System.lineSeparator() +
                    "Public key (n,e) = (" + publicKey[0] + ", " + publicKey[1] + ")";
            result = output;
            gcd = rsa.gcd(new BigInteger(numberP), new BigInteger(numberQ));
            BigInteger[] numbers = rsa.advancedEuclidean(new BigInteger(numberP), new BigInteger(numberQ));
            euc = "r0 = " + numbers[1] + ", r1 = " + numbers[2];

        }
        else {
            rsa = new RSA(numberN, new BigInteger(numberE));
            result = rsa.decrypt(plain_text);
        }

        if(toFile != null) {
            FileWriter writer = new FileWriter("C:\\Users\\Dasha\\IdeaProjects\\RSAalgorithm\\output.txt", false);
            writer.write(output);
            writer.close();
        }

        return "redirect:/";
    }
}

