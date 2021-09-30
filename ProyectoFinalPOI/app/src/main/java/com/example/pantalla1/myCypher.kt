package com.example.pantalla1

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object myCypher {

    private const val CIPHER_TRANSFORM = "AES/CBC/PKCS5PADDING"

    fun cifrar(plainText: String, llave: String) : String{

        val cipher = Cipher.getInstance(CIPHER_TRANSFORM)

        val llaveBytesFinal = ByteArray(16)
        val llaveBytesOriginal = llave.toByteArray(charset("UTF-8"))

        System.arraycopy(
            llaveBytesOriginal,
            0,
            llaveBytesFinal,
            0,
            Math.min(llaveBytesOriginal.size, llaveBytesFinal.size)
        )

        val secretKeySpec = SecretKeySpec(
            llaveBytesFinal,
            "AES"
        )
        val vectorInit = IvParameterSpec(llaveBytesFinal)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            secretKeySpec,
            vectorInit
        )

        val textoCifrado = cipher.doFinal(
            plainText.toByteArray(
                charset("UTF-8")
            )
        )

        var resultadoString = String(textoCifrado)

        var resultado64 = String(Base64.encode(textoCifrado, Base64.NO_PADDING))

        return resultado64;
    }

    fun descifrar( textoCifrado: String, llave: String) : String{

        val textoCifradoBytes = Base64.decode(textoCifrado, Base64.NO_PADDING)

        val cipher = Cipher.getInstance(CIPHER_TRANSFORM)

        val llaveBytesFinal = ByteArray(16)
        val llaveBytesOriginal = llave.toByteArray(charset("UTF-8"))

        System.arraycopy(
            llaveBytesOriginal,
            0,
            llaveBytesFinal,
            0,
            Math.min(llaveBytesOriginal.size, llaveBytesFinal.size)
        )

        val secretKeySpec = SecretKeySpec(
            llaveBytesFinal,
            "AES"
        )
        val vectorInit = IvParameterSpec(llaveBytesFinal)

        cipher.init(
            Cipher.DECRYPT_MODE,
            secretKeySpec,
            vectorInit
        )

        val textoPlano = String(cipher.doFinal(
            textoCifradoBytes
        )
        )

        return  textoPlano
    }
}