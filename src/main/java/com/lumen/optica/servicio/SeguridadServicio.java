/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lumen.optica.servicio;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Alexander
 */
public class SeguridadServicio {

    private static final int ITERACIONES = 600000;
    private static final int TAMANO_SALT = 16;
    private static final int TAMANO_HASH = 32;

    private final SecureRandom random = new SecureRandom();

    public String generarHash(String contrasena) {
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException(
                    "La contraseña no puede estar vacía.");
        }

        byte[] salt = new byte[TAMANO_SALT];
        random.nextBytes(salt);

        byte[] hash = calcularHash(contrasena.toCharArray(), salt);

        return "PBKDF2$" + ITERACIONES + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(hash);
    }

    public boolean verificarContrasena(
            String contrasena, String hashAlmacenado) {

        if (contrasena == null || hashAlmacenado == null) {
            return false;
        }

        try {
            String[] partes = hashAlmacenado.split("\\$");

            if (partes.length != 4 || !partes[0].equals("PBKDF2")) {
                return false;
            }

            int iteraciones = Integer.parseInt(partes[1]);

            if (iteraciones < 1 || iteraciones > 2000000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(partes[2]);
            byte[] hashOriginal = Base64.getDecoder().decode(partes[3]);

            if (salt.length < 16 || hashOriginal.length != TAMANO_HASH) {
                return false;
            }

            byte[] hashIngresado = calcularHash(
                    contrasena.toCharArray(), salt, iteraciones);

            return java.security.MessageDigest.isEqual(
                    hashOriginal, hashIngresado);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private byte[] calcularHash(char[] contrasena, byte[] salt) {
        return calcularHash(contrasena, salt, ITERACIONES);
    }

    private byte[] calcularHash(
            char[] contrasena, byte[] salt, int iteraciones) {

        PBEKeySpec especificacion = new PBEKeySpec(
                contrasena, salt, iteraciones, TAMANO_HASH * 8);

        try {
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance(
                    "PBKDF2WithHmacSHA256");

            return fabrica.generateSecret(especificacion).getEncoded();

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "No se pudo procesar la contraseña.", e);
        } finally {
            especificacion.clearPassword();
            java.util.Arrays.fill(contrasena, '\0');
        }
    }
}