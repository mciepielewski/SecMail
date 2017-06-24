package com.example.sec.sec;


import android.hardware.fingerprint.FingerprintManager;

import org.spongycastle.bcpg.ArmoredInputStream;
import org.spongycastle.bcpg.ArmoredOutputStream;
import org.spongycastle.bcpg.BCPGInputStream;
import org.spongycastle.bcpg.HashAlgorithmTags;
import org.spongycastle.bcpg.SignatureSubpacket;
import org.spongycastle.bcpg.SignatureSubpacketTags;
import org.spongycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.spongycastle.bcpg.sig.Features;
import org.spongycastle.bcpg.sig.KeyFlags;
import org.spongycastle.bcpg.sig.PreferredAlgorithms;
import org.spongycastle.crypto.generators.RSAKeyPairGenerator;
import org.spongycastle.crypto.params.RSAKeyGenerationParameters;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.openpgp.PGPCompressedData;
import org.spongycastle.openpgp.PGPCompressedDataGenerator;
import org.spongycastle.openpgp.PGPEncryptedData;
import org.spongycastle.openpgp.PGPEncryptedDataGenerator;
import org.spongycastle.openpgp.PGPEncryptedDataList;
import org.spongycastle.openpgp.PGPException;
import org.spongycastle.openpgp.PGPKeyPair;
import org.spongycastle.openpgp.PGPKeyRingGenerator;
import org.spongycastle.openpgp.PGPLiteralData;
import org.spongycastle.openpgp.PGPLiteralDataGenerator;

import org.spongycastle.openpgp.PGPObjectFactory;
import org.spongycastle.openpgp.PGPPrivateKey;
import org.spongycastle.openpgp.PGPPublicKey;
import org.spongycastle.openpgp.PGPPublicKeyEncryptedData;
import org.spongycastle.openpgp.PGPPublicKeyRing;
import org.spongycastle.openpgp.PGPSecretKey;
import org.spongycastle.openpgp.PGPSecretKeyRing;
import org.spongycastle.openpgp.PGPSignature;
import org.spongycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.spongycastle.openpgp.PGPSignatureSubpacketVector;
import org.spongycastle.openpgp.PGPUtil;
import org.spongycastle.openpgp.jcajce.JcaPGPPublicKeyRing;
import org.spongycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection;
import org.spongycastle.openpgp.jcajce.JcaPGPSecretKeyRing;
import org.spongycastle.openpgp.operator.KeyFingerPrintCalculator;
import org.spongycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.spongycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.spongycastle.openpgp.operator.PGPDigestCalculator;
import org.spongycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.spongycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.spongycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.spongycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.spongycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.spongycastle.openpgp.operator.bc.BcPGPKeyPair;
import org.spongycastle.openpgp.operator.bc.BcPublicKeyDataDecryptorFactory;
import org.spongycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.spongycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.spongycastle.util.encoders.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Kamil on 07.12.2016.
 */
public class PgpUtils {
    private static final String PROVIDER = "SC";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    public final static String genPGPPublicKey (PGPKeyRingGenerator krgen){
        ByteArrayOutputStream baosPkr = null;
        try {
            baosPkr = new ByteArrayOutputStream();
            PGPPublicKeyRing pkr = krgen.generatePublicKeyRing();
            ArmoredOutputStream armoredStreamPkr = new ArmoredOutputStream(baosPkr);
            pkr.encode(armoredStreamPkr);
            armoredStreamPkr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return new String(baosPkr.toByteArray(), Charset.defaultCharset());
    }

    public final static String genPGPPrivKey (PGPKeyRingGenerator krgen){
        ByteArrayOutputStream baosPriv = new ByteArrayOutputStream();
        try {
            PGPSecretKeyRing skr = krgen.generateSecretKeyRing();
            ArmoredOutputStream armoredStreamPriv = new ArmoredOutputStream(baosPriv);
            skr.encode(armoredStreamPriv);
            armoredStreamPriv.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return new String(baosPriv.toByteArray(), Charset.defaultCharset());
    }

    public static String encryptMessage(String msg, String key){
        String ciphertext = null;
        try {
            InputStream in = new ByteArrayInputStream(key.getBytes());
            in = org.spongycastle.openpgp.PGPUtil.getDecoderStream(in);

            JcaPGPPublicKeyRing pgpPub = new JcaPGPPublicKeyRing(in);
            in.close();
            Iterator<PGPPublicKey> pks = pgpPub.getPublicKeys();
            PGPPublicKey keyPGP = null;

            int[] preferredSymmetricAlgorithms = null;
            while(pks.hasNext()){
                PGPPublicKey pk = pks.next();
                if (pk.isEncryptionKey()){
                    keyPGP = pk;
                    break;
                }
                if (pk.isMasterKey()){
                    @SuppressWarnings("rawtypes")
                    Iterator v = pk.getSignatures();
                    while (v.hasNext()) {
                        PGPSignature sig = (PGPSignature)v.next();
                        PGPSignatureSubpacketVector hashedSubPackets = sig.getHashedSubPackets();
                        preferredSymmetricAlgorithms = getPreferredSymmetricAlgorithms(hashedSubPackets);
                    }
                }
            }

            int preferredSymAlgo = PGPEncryptedData.AES_256;
            if (preferredSymmetricAlgorithms != null && preferredSymmetricAlgorithms.length != 0)
                preferredSymAlgo = preferredSymmetricAlgorithms[0];
            ciphertext = encryptText(msg, keyPGP,preferredSymAlgo);

            }catch(IOException e){
                e.printStackTrace();
            }
        return ciphertext;
    }

    private static String encryptText(String sIn, PGPPublicKey encKey, int symAlgo){
        ByteArrayOutputStream encOut=null;
        try {
            byte[] clearData = sIn.getBytes();
            encOut = new ByteArrayOutputStream();
            OutputStream out = new ArmoredOutputStream(encOut);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(PGPCompressedDataGenerator.ZIP);
            OutputStream cos = comData.open(bOut);
            PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();
            OutputStream pOut = lData.open(cos, PGPLiteralData.BINARY, PGPLiteralData.CONSOLE, clearData.length, new Date());
            pOut.write(clearData);
            lData.close();
            comData.close();
            PGPEncryptedDataGenerator encGen =
                    new PGPEncryptedDataGenerator(
                            new JcePGPDataEncryptorBuilder(PGPEncryptedData.AES_256).setWithIntegrityPacket(true).setSecureRandom(
                                    new SecureRandom()).setProvider("SC"));
            if (encKey != null) {
                encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encKey).setProvider("SC"));
                byte[] bytes = bOut.toByteArray();
                OutputStream cOut = encGen.open(out, bytes.length);
                cOut.write(bytes);
                cOut.close();
            }
            out.close();
        }catch(IOException | PGPException e){
            e.printStackTrace();
        }
        return new String(encOut.toByteArray());
    }

    public static String decrypt(String encryptedText, String key, String password) {
        try {
            byte[] encrypted = encryptedText.getBytes();
            InputStream in = new ByteArrayInputStream(encrypted);
            in = PGPUtil.getDecoderStream(in);
            PGPObjectFactory pgpF = new PGPObjectFactory(in, new BcKeyFingerprintCalculator());
            PGPEncryptedDataList enc;
            Object o = pgpF.nextObject();
            if (o instanceof PGPEncryptedDataList) {
                enc = (PGPEncryptedDataList) o;
            } else {
                enc = (PGPEncryptedDataList) pgpF.nextObject();
            }
            PGPPrivateKey sKey = null;
            PGPPublicKeyEncryptedData pbe = null;
            while (sKey == null && enc.getEncryptedDataObjects().hasNext()) {
                pbe = (PGPPublicKeyEncryptedData) enc.getEncryptedDataObjects().next();
                sKey = getPrivateKey(getPGPSecretKeyRing(key), pbe.getKeyID(), password.toCharArray());
            }
            if (pbe != null) {
                InputStream clear = pbe.getDataStream(new BcPublicKeyDataDecryptorFactory(sKey));
                PGPObjectFactory pgpFact = new PGPObjectFactory(clear, new BcKeyFingerprintCalculator());
                PGPCompressedData cData = (PGPCompressedData) pgpFact.nextObject();
                pgpFact = new PGPObjectFactory(cData.getDataStream(), new BcKeyFingerprintCalculator());
                PGPLiteralData ld = (PGPLiteralData) pgpFact.nextObject();
                InputStream unc = ld.getInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int ch;
                while ((ch = unc.read()) >= 0) {
                    out.write(ch);
                }
                byte[] returnBytes = out.toByteArray();
                out.close();
                return new String(returnBytes);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static PGPPrivateKey getPrivateKey(PGPSecretKeyRing keyRing, long keyID, char[] pass) throws PGPException {
        PGPSecretKey secretKey = keyRing.getSecretKey(keyID);
        PBESecretKeyDecryptor decryptor = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(pass);
        return secretKey.extractPrivateKey(decryptor);
    }

    private static PGPSecretKeyRing getPGPSecretKeyRing(String key) throws IOException {
        PGPSecretKeyRing decKeyRing = null;
        try {
            InputStream in = new ByteArrayInputStream(key.getBytes());
            in = PGPUtil.getDecoderStream(in);
            BCPGInputStream inB = new BCPGInputStream(in);

            decKeyRing = new PGPSecretKeyRing(inB,
                    new BcKeyFingerprintCalculator());
            in.close();
        }catch(IOException | PGPException e){
            e.printStackTrace();
        }
        return decKeyRing;
    }

    public final static PGPKeyRingGenerator generateKeyRingGenerator (char[] pass, String email){
        PGPKeyRingGenerator keyRingGen = null;
        try {
            RSAKeyPairGenerator kpg = new RSAKeyPairGenerator();
            kpg.init(new RSAKeyGenerationParameters(BigInteger.valueOf(0x10001), new SecureRandom(), 2048, 12));
            PGPKeyPair rsakp_sign = new BcPGPKeyPair(PGPPublicKey.RSA_SIGN, kpg.generateKeyPair(), new Date());
            PGPKeyPair rsakp_enc = new BcPGPKeyPair(PGPPublicKey.RSA_ENCRYPT, kpg.generateKeyPair(), new Date());
            PGPSignatureSubpacketGenerator signhashgen = new PGPSignatureSubpacketGenerator();
            signhashgen.setKeyFlags(false, KeyFlags.SIGN_DATA | KeyFlags.CERTIFY_OTHER | KeyFlags.SHARED);
            signhashgen.setPreferredSymmetricAlgorithms(false, new int[]{SymmetricKeyAlgorithmTags.AES_256, SymmetricKeyAlgorithmTags.AES_192, SymmetricKeyAlgorithmTags.AES_128});
            signhashgen.setPreferredHashAlgorithms(false, new int[]{HashAlgorithmTags.SHA256, HashAlgorithmTags.SHA1, HashAlgorithmTags.SHA384, HashAlgorithmTags.SHA512, HashAlgorithmTags.SHA224});
            signhashgen.setFeature(false, Features.FEATURE_MODIFICATION_DETECTION);
            PGPSignatureSubpacketGenerator enchashgen = new PGPSignatureSubpacketGenerator();
            enchashgen.setKeyFlags(false, KeyFlags.ENCRYPT_COMMS | KeyFlags.ENCRYPT_STORAGE);
            PGPDigestCalculator sha1Calc = new BcPGPDigestCalculatorProvider().get(HashAlgorithmTags.SHA1);
            PGPDigestCalculator sha256Calc = new BcPGPDigestCalculatorProvider().get(HashAlgorithmTags.SHA256);
            PBESecretKeyEncryptor pske = (new BcPBESecretKeyEncryptorBuilder(PGPEncryptedData.AES_256, sha256Calc, 0xc0)).build(pass);
            keyRingGen = new PGPKeyRingGenerator(PGPSignature.POSITIVE_CERTIFICATION, rsakp_sign,
                    email, sha1Calc, signhashgen.generate(), null, new BcPGPContentSignerBuilder(rsakp_sign.getPublicKey().getAlgorithm(),
                    HashAlgorithmTags.SHA1), pske);
            keyRingGen.addSubKey(rsakp_enc, enchashgen.generate(), null);

        }catch(PGPException e){
            e.printStackTrace();
        }
        return keyRingGen;
    }

    public static int[] getPreferredSymmetricAlgorithms(PGPSignatureSubpacketVector attributes) {
        SignatureSubpacket p = attributes.getSubpacket(SignatureSubpacketTags.PREFERRED_SYM_ALGS);

        if (p == null) {
            return null;
        }

        return ((PreferredAlgorithms) p).getPreferences();
    }

}
