package com.hit.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IAlgoEncryptionTest {

    private static final String ENCRYPT_EX_1 = "";
    private static final String ENCRYPT_EX_2 = "Ab123_./*+-=!@#$%^&()";
    private static final String ENCRYPT_EX_3 = "kajsd grfoiu 21t3789o4 vty1298p59iusfljkhv87021" +
            " 349p08likudfagliu aso;id hfp9o8qyw630894 awodfgjyhklzx dgtfpo8i2q yu39p4 hqoiuy7wetrf908 " +
            "q234hbjqhl2 g34bp98q2nu9pfoisadvoifu32 4go8pi7u23 g49ri7u wa9i8sdfty98a ysdfoiahskldj fgaskj";
    private static final String ENCRYPT_EX_4 = null;

    private void TestGeneral(IAlgoEncryption algo)
    {
        byte[] test1 = algo.encrypt(ENCRYPT_EX_1);
        assertEquals(algo.decrypt(test1), ENCRYPT_EX_1);

        byte[] test2 = algo.encrypt(ENCRYPT_EX_2);
        assertEquals(algo.decrypt(test2), ENCRYPT_EX_2);

        byte[] test3 = algo.encrypt(ENCRYPT_EX_3);
        assertEquals(algo.decrypt(test3), ENCRYPT_EX_3);

        byte[] test4 = algo.encrypt(ENCRYPT_EX_4);
        assertEquals(algo.decrypt(test4), ENCRYPT_EX_4);
    }

    @Test
    public void TestAES()
    {
        AESSymmetricAlgoEncryptionImpl aes = new AESSymmetricAlgoEncryptionImpl();
        TestGeneral(aes);
    }

    @Test
    public void TestRSA()
    {
        RSAAsymmetricAlgoCacheImpl rsa = new RSAAsymmetricAlgoCacheImpl();
        TestGeneral(rsa);
    }
}