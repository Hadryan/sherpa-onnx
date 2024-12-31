package com.k2fsa.sherpa.onnx

fun main() {
  testVits()
  testMatcha()
}

fun testMatcha() {
  // see https://github.com/k2-fsa/sherpa-onnx/releases/tag/tts-models
  // https://github.com/k2-fsa/sherpa-onnx/releases/download/tts-models/matcha-icefall-zh-baker.tar.bz2
  var config = OfflineTtsConfig(
    model=OfflineTtsModelConfig(
      matcha=OfflineTtsMatchaModelConfig(
        acousticModel="./matcha-icefall-zh-baker/model-steps-3.onnx",
        vocoder="./hifigan_v2.onnx",
        tokens="./matcha-icefall-zh-baker/tokens.txt",
        lexicon="./matcha-icefall-zh-baker/lexicon.txt",
        dictDir="./matcha-icefall-zh-baker/dict",
      ),
      numThreads=1,
      debug=true,
    ),
    ruleFsts="./matcha-icefall-zh-baker/phone.fst,./matcha-icefall-zh-baker/date.fst,./matcha-icefall-zh-baker/number.fst",
  )
  val tts = OfflineTts(config=config)
  val audio = tts.generateWithCallback(text="某某银行的副行长和一些行政领导表示，他们去过长江和长白山; 经济不断增长。2024年12月31号，拨打110或者18920240511。123456块钱。", callback=::callback)
  audio.save(filename="test-zh.wav")
  tts.release()
  println("Saved to test-zh.wav")
}

fun testVits() {
  // see https://github.com/k2-fsa/sherpa-onnx/releases/tag/tts-models
  // https://github.com/k2-fsa/sherpa-onnx/releases/download/tts-models/vits-piper-en_US-amy-low.tar.bz2
  var config = OfflineTtsConfig(
    model=OfflineTtsModelConfig(
      vits=OfflineTtsVitsModelConfig(
        model="./vits-piper-en_US-amy-low/en_US-amy-low.onnx",
        tokens="./vits-piper-en_US-amy-low/tokens.txt",
        dataDir="./vits-piper-en_US-amy-low/espeak-ng-data",
      ),
      numThreads=1,
      debug=true,
    )
  )
  val tts = OfflineTts(config=config)
  val audio = tts.generateWithCallback(text="“Today as always, men fall into two groups: slaves and free men. Whoever does not have two-thirds of his day for himself, is a slave, whatever he may be: a statesman, a businessman, an official, or a scholar.”", callback=::callback)
  audio.save(filename="test-en.wav")
  tts.release()
  println("Saved to test-en.wav")
}

/*
1. Unzip test_tts.jar
2.
javap ./com/k2fsa/sherpa/onnx/Test_ttsKt\$testTts\$audio\$1.class

3. It prints:
Compiled from "test_tts.kt"
final class com.k2fsa.sherpa.onnx.Test_ttsKt$testTts$audio$1 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function1<float[], java.lang.Integer> {
  public static final com.k2fsa.sherpa.onnx.Test_ttsKt$testTts$audio$1 INSTANCE;
  com.k2fsa.sherpa.onnx.Test_ttsKt$testTts$audio$1();
  public final java.lang.Integer invoke(float[]);
  public java.lang.Object invoke(java.lang.Object);
  static {};
}

4.
javap -s ./com/k2fsa/sherpa/onnx/Test_ttsKt\$testTts\$audio\$1.class

5. It prints
Compiled from "test_tts.kt"
final class com.k2fsa.sherpa.onnx.Test_ttsKt$testTts$audio$1 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function1<float[], java.lang.Integer> {
  public static final com.k2fsa.sherpa.onnx.Test_ttsKt$testTts$audio$1 INSTANCE;
    descriptor: Lcom/k2fsa/sherpa/onnx/Test_ttsKt$testTts$audio$1;
  com.k2fsa.sherpa.onnx.Test_ttsKt$testTts$audio$1();
    descriptor: ()V

  public final java.lang.Integer invoke(float[]);
    descriptor: ([F)Ljava/lang/Integer;

  public java.lang.Object invoke(java.lang.Object);
    descriptor: (Ljava/lang/Object;)Ljava/lang/Object;

  static {};
    descriptor: ()V
}
*/
fun callback(samples: FloatArray): Int {
  println("callback got called with ${samples.size} samples");

  // 1 means to continue
  // 0 means to stop
  return 1
}
