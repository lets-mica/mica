import 'package:encrypt/encrypt.dart';

void main(List<String> arguments) {
  // 明文数据和密钥
  final plainText = '{"auPhone":"10008376857","auPassword":"123456"}';
  final plainKey = 'eELB8HbcgKWkndH0g9qRcBMZG5zgPkMc';

  // 初始化加密工具
  final key = Key.fromUtf8(plainKey);
  final iv = IV.fromUtf8(plainKey.substring(0, 16));
  final encrypter = Encrypter(AES(key, mode: AESMode.cbc));

  // dart 端加密
  final encrypted = encrypter.encrypt(plainText, iv: iv);
  print(encrypted.bytes);
  print(encrypted.base64);

  // 解密 java 端密文 AesUtil.encryptToBase64
  final javaEncryptedText = 'BLo1Q73NNBWMxpqBF66sKrI+PYfikhD5YU2O0A+tkBRuiy1FkPxSnxUHw5rV2QZK';
  final decrypted = encrypter.decrypt64(javaEncryptedText, iv: iv);

  print(decrypted);
}

