/**
 * 使用：https://github.com/brix/crypto-js
 *
 * 或者直接导入下面这2个 js
 * <script src="https://cdn.bootcss.com/crypto-js/4.0.0/crypto-js.min.js"></script>
 * <script src="https://cdn.bootcss.com/crypto-js/4.0.0/aes.min.js"></script>
 */

/**
 * des 解密方法，同java：DesUtil.encryptToBase64(text, aesKey)
 */
function encrypt(data, key) {
    var keyHex = CryptoJS.enc.Utf8.parse(key);
    var encrypted = CryptoJS.DES.encrypt(data, keyHex, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
}

/**
 * des 加密方法，同java：DesUtil.decryptFormBase64(encryptBase64, aesKey);
 */
function decrypt(data, key) {
    var keyHex = CryptoJS.enc.Utf8.parse(key);
    var decrypted = CryptoJS.DES.decrypt({
        ciphertext: CryptoJS.enc.Base64.parse(data)
    }, keyHex, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    return decrypted.toString(CryptoJS.enc.Utf8);
}

console.log('des 加密', encrypt('我爱mica', 'QLPz39vD2Nkf9A4E'))
console.log('des 解密', decrypt('49o5mK4KQGGAYY4IwoOG4A==', 'QLPz39vD2Nkf9A4E'))
