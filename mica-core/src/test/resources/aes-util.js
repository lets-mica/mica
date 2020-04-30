/**
 * 使用：https://github.com/brix/crypto-js
 *
 * 或者直接导入下面这2个 js
 * <script src="https://cdn.bootcss.com/crypto-js/4.0.0/crypto-js.min.js"></script>
 * <script src="https://cdn.bootcss.com/crypto-js/4.0.0/aes.min.js"></script>
 */

/**
 * aes 解密方法，同java：AesUtil.decryptFormBase64ToString(encrypt, aesKey);
 */
function decrypt(data, key) {
    let keyBytes = CryptoJS.enc.Utf8.parse(key);
    let decrypted = CryptoJS.AES.decrypt(data, keyBytes, {
        iv: keyBytes,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    return CryptoJS.enc.Utf8.stringify(decrypted);
}

/**
 * aes 加密方法，同java：AesUtil.encryptToBase64(text, aesKey);
 */
function encrypt(data, key) {
    let dataBytes = CryptoJS.enc.Utf8.parse(data);
    let keyBytes = CryptoJS.enc.Utf8.parse(key);
    let encrypted = CryptoJS.AES.encrypt(dataBytes, keyBytes, {
        iv: keyBytes,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    return CryptoJS.enc.Base64.stringify(encrypted.ciphertext);
}

console.log('aes 加密', encrypt('我爱mica', 'dIK8YXdYaVasUyq1LvUoxCpgPKthHtAW'))
console.log('aes 解密', decrypt('m44gDqhWr86Vl1V0ORNShA==', 'dIK8YXdYaVasUyq1LvUoxCpgPKthHtAW'))
