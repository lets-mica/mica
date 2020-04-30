/**
 * 使用：https://github.com/travist/jsencrypt
 *
 * 或者直接导入下面这个 js
 * <script src="https://cdn.bootcss.com/jsencrypt/2.3.0/jsencrypt.js"></script>
 */

/**
 * 前端加密
 *
 * <p>
 * 对应java解密方法为：
 * String decrypt = RsaUtil.decryptFromBase64(privateBase64, data);
 * </p>
 */
function encrypt(data, publicKey) {
    var encrypt = new JSEncrypt();
    encrypt.setPublicKey(publicKey);
    return encrypt.encrypt(data);
}

// 注意：解密老解不出来，不折腾了，谁有需求到时候再研究
