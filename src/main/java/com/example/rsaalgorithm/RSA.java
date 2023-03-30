package com.example.rsaalgorithm;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;

public class RSA {

    private BigInteger p; // the first prime number
    private BigInteger q; // the second prime number
    private BigInteger N; // modulus
    private BigInteger phi; // totient
    private BigInteger e; // public key
    private BigInteger d; // private key
    public RSA() {

    }

    public RSA(BigInteger p, BigInteger q) {
        this.p = p;
        this.q = q;
        N = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = findPublicKey(phi);
        d = findPrivateKey(e, phi);
    }

    public RSA(String N, BigInteger e) {
        BigInteger[] factors = primeFactors(new BigInteger(N));
        this.p = factors[0];
        this.q = factors[1];
        this.N = new BigInteger(N);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        this.e = e;
        d = findPrivateKey(e, phi);
    }

    public String encrypt(String message) {
        String[] substrings = message.split(" ");
        BigInteger[] numbers = new BigInteger[substrings.length];
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < substrings.length; i++) {
            numbers[i] = new BigInteger(substrings[i]);
            numbers[i] = numbers[i].modPow(e, N);
            output.append(numbers[i]);
            output.append(" ");
        }
        return output.toString();
    }
    public String decrypt(String message) {
        String[] substrings = message.split(" ");
        BigInteger[] numbers = new BigInteger[substrings.length];
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < substrings.length; i++) {
            numbers[i] = new BigInteger(substrings[i]);
            numbers[i] = numbers[i].modPow(d, N);
            output.append(numbers[i]);
            output.append(" ");
        }
        return output.toString();
    }

    public BigInteger[] getPublicKey() {
        return new BigInteger[]{N, e};
    }

    public BigInteger[] advancedEuclidean(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return new BigInteger[]{a, BigInteger.ONE, BigInteger.ZERO};
        } else {
            BigInteger[] results = advancedEuclidean(b, a.mod(b));
            BigInteger gcd = results[0];
            BigInteger x = results[2];
            BigInteger y = results[1].subtract(a.divide(b).multiply(results[2]));
            return new BigInteger[]{gcd, x, y};
        }
    }

    private static BigInteger[] primeFactors(BigInteger n) {
        BigInteger[] factors = new BigInteger[2];
        BigInteger i = BigInteger.valueOf(2);
        while (n.mod(i).intValue() != 0) {
            i = i.add(BigInteger.ONE);
        }
        factors[0] = i;
        factors[1] = n.divide(i);
        return factors;
    }

    public BigInteger findPublicKey(BigInteger phi) {
        BigInteger e = BigInteger.valueOf(2);
        while (!gcd(e, phi).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.ONE);
        }
        return e;
    }

    public static BigInteger findPrivateKey(BigInteger e, BigInteger phi) {
        BigInteger dBig = e.modInverse(phi);
        return dBig;
    }

    public BigInteger gcd(BigInteger a, BigInteger b) {
        a = a.abs();
        b = b.abs();

        // Return the GCD using Euclid's algorithm
        while (!b.equals(BigInteger.ZERO)) {
            BigInteger temp = b;
            b = a.mod(b);
            a = temp;
        }

        return a;
    }
}


