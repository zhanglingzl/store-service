package com.rxr.store.biz.util;

import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyUtil {
    public static PrivateKey PRIVATE_KEY = null;
    public static PublicKey PUBLIC_KEY = null;
    /**产品二维码RSA公钥(Base64编码)*/
    public final static String PRODUCT_QR_CODE_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ" +
            "8AMIIBCgKCAQEAl2w0FvFYReQM0zu3Eer2oTaPF3MnzjZY" +
            "fNWdltzwLRjrtYe/WoNHK9R5YTTI1aK6KjDfulfdCs9SRfrhn5P6s3ilCv9al9cdxx4DlutZzWkH" +
            "J3YhkcJ2+h7tTFBCtM09hNcvgyWLZYj36KJSv01uG1Ljzpd+iCMC+UUIDBi1ysFcBSV9KghJ6/gM" +
            "UnuXfY9xOxKtayvqmq06tgmtD1jexlFtdQqVIFgs9zpsiYC5MF3nf/oSsxnQuoM+JQlJKOB8x57p" +
            "MUbAs+fE0rnHOWiYrGB2/T8mRivQw5BXepgU1eUFz9zxAMPmUZsJU/b9cwWwFduI2CNHvcjSXPO/" +
            "aLhTjQIDAQAB";

    /**产品二维码RSA私钥(Base64编码)*/
    public final static String PRODUCT_QR_CODE_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQE" +
            "FAASCBKcwggSjAgEAAoIBAQCXbDQW8VhF5AzTO7cR6vahNo8X" +
            "cyfONlh81Z2W3PAtGOu1h79ag0cr1HlhNMjVoroqMN+6V90Kz1JF+uGfk/qzeKUK/1qX1x3HHgOW" +
            "61nNaQcndiGRwnb6Hu1MUEK0zT2E1y+DJYtliPfoolK/TW4bUuPOl36IIwL5RQgMGLXKwVwFJX0q" +
            "CEnr+AxSe5d9j3E7Eq1rK+qarTq2Ca0PWN7GUW11CpUgWCz3OmyJgLkwXed/+hKzGdC6gz4lCUko" +
            "4HzHnukxRsCz58TSucc5aJisYHb9PyZGK9DDkFd6mBTV5QXP3PEAw+ZRmwlT9v1zBbAV24jYI0e9" +
            "yNJc879ouFONAgMBAAECggEAFESDZF7BdflkThG8G0Xb5RAsbvMW7N8RUQQ2p67C5wZi5lVZI4BW" +
            "0mMKLByolQ/G40bPa12/VGcTmBJQiM+ByuvnC2XKdnBwp+o4iMbPqlLYhkuliqEA09G36XMEuraN" +
            "+W3kCiXIVrrSk+zGLpzqk/WwUEKuyQIGr96xiQz1qEGrWAssgjOz/MWLeVZhIBBZxLlrjBblJNeF" +
            "+b+84Uron/nJ7ofD2h4dfdYrx7frCd8ltlQfFcpoBSy+io+5onOSfIYgsQYHhZaX1XEM/Llag8bh" +
            "fDqvStq4NHAXhUm87EFkHSsIdhGIztto0rFiwNZPMQyMOG2CYlVm8qbfwzO8cQKBgQD1OqgpRMsY" +
            "HMtyPajR4mX1RDlV38qTTOoupooBB0FMyBAD4EGLM7JzWHvR63EY5E/Ilbgq1Drc3SYwwbGLEzB5" +
            "K2vLvgV8cbAEAg1ZdpJ8lS0buQgjvku9BWUa0W7Dabn7LHen/x7XuW+VI2yPUIaBEsUQOfnGGtZa" +
            "wD7asJ4P7wKBgQCeEsq0+phq4P1vCemyY+c98lHr3uQqQG9EDmXp3wHrGDeNDzDdCw7MTJKqFopZ" +
            "2oZeueAoQKbAC+sgjYjh295Unxmhnnv37NKefndcMvNQ0mcmaVTodDSBCA20RiIh+EymhYlPC2Rr" +
            "J2W2yLLlI7/4g/iMyNYxNew+LUnG5CBYQwKBgGUsBACyrw+NoanKIoIk42ei8/ynzY2j5+HDN6Bg" +
            "++HPkDvpI2FovLC1yq+6cPiSCRB6JHcXBAnm+0CWSMdV7WOwNFLkXKMQzEt7BULpllnruaZ8Wg1V" +
            "Dy42sZW03fuk/OPRXrRinrjlelGPQdSGpldOBRtCr/e5TsI2GjhjMXLlAoGAP5KFLZXe+JT1t9N/" +
            "xjvBgztSUQ86KffyB6fWuuDyT49UwAlM+Ij68ZUWIwufppVpFu2gRfACYBgq/tONKnCXkObPkx0+" +
            "9C2tYwqvjEf9bRLBY0RXXollmEMZ59eQ4KKQiSQJXd3SC/ozQPikYd0n1pWUv2ICKpZqJXNnSXLz" +
            "wysCgYEAlS6zIZn0X24Ztro+kft3mUOdcXHfftAMOHGqdasf3kLFa2DSWuY5Y7OdGrWL1l8qmQRy" +
            "0ZlO6iHM7jAbbdOM3MmEupx4cUhV41HGrUmvZsQfKHvBiO4oxvGw6LJRtlPsHQFPIt3GBnI5Hzw2" +
            "BOHBmmW0FcrSWicawSR0jtZJkek=";
    public static void main(String[] args) throws Exception {
        String ss = encrypt("测试");
        System.out.println(ss);
        System.out.println(decrypt(ss));
    }

    /**
     * 加密
     * @param encrypt
     * @return
     */
    public static String encrypt(String encrypt) {
        String secEncrypt = "";
        try {
            if(PUBLIC_KEY == null)  {
                PUBLIC_KEY = RSAUtil.string2PublicKey(PRODUCT_QR_CODE_PUBLIC_KEY);
            }
            secEncrypt = RSAUtil.byte2Base64(RSAUtil.publicEncrypt(encrypt.getBytes(), PUBLIC_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return secEncrypt;

    }

    /**
     * 解密
     * @param decrypt
     * @return
     */
    public static String decrypt(String decrypt) {
        String secDecrypt = "";
        try {
            if(PRIVATE_KEY == null)  {
                PRIVATE_KEY = RSAUtil.string2PrivateKey(PRODUCT_QR_CODE_PRIVATE_KEY);

            }
            secDecrypt = new String(RSAUtil.privateDecrypt(RSAUtil.base642Byte(decrypt), PRIVATE_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secDecrypt;
    }
}
