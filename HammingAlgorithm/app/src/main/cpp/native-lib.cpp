#include <jni.h>
#include <string>
#include <vector>
#include <sstream>
#include <math.h>

template <typename T>
std::string to_string(T value)
{
    std::ostringstream os ;
    os << value ;
    return os.str() ;
}


extern "C"
JNIEXPORT jstring

JNICALL
Java_discretos_tec_hammingalgorithm_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


std::string ConvertJString(JNIEnv* env, jstring str)
{
    const jsize len = env->GetStringUTFLength(str);
    const char* strChars = env->GetStringUTFChars(str, (jboolean *)0);

    std::string Result(strChars, len);

    env->ReleaseStringUTFChars(str, strChars);

    return Result;
}

std::string getResultantData(std::vector<std::string> vec){
    std::string result ="";// (std::string)vec.front();
    /*result = result.substr(2);
    for(std::string str : vec){
        int i = 0;
        for(char ch :  str.substr(2)){
            if(ch != result.at(i))result = result.substr(0,i) + to_string(ch) + result.substr(i+1);
        }
    }*/
    return result;
}

extern "C"
JNIEXPORT jobjectArray
JNICALL
Java_discretos_tec_hammingalgorithm_MainActivity_encode(JNIEnv *env,
                                                        jobject jobj,
                                                        jstring inputData,
                                                        jchar parity){

    jobjectArray ret;
    std::string data = ConvertJString( env, inputData );
    std::string encodedData = data;
    std::vector<std::string>* vecResult = new std::vector<std::string>();
    int b = 1;
    for(int i=0 ; i < data.length() ; i++){
        if((i+1) > 0 && (((i+1) & ((i+1) - 1)) == 0)){
            std::string result = "P"+to_string(b);
            int count = 0;
            for(int j = 0 ; j < data.substr(i).length() ; j+=b+1){

                if (data.substr(i).c_str()[j] == '1') {
                    count++;
                }

            }
            if(count % 2 == 0){
                result += data.substr(0,i);
                result += parity;
                result += data.substr(i+1);
                //encodedData = encodedData.substr(0,i) + to_string(parity) + encodedData.substr(i);
            }else{
                if(parity == '1'){
                    result += data.substr(0,i) + std::string("0") +data.substr(i+1);
                   // encodedData  = encodedData.substr(0,i) + std::string("0") +encodedData.substr(i);
                }
                if(parity == '0'){
                    result += data.substr(0,i) + std::string("1") +data.substr(i+1);
                   // encodedData = encodedData.substr(0,i) + std::string("1") +encodedData.substr(i);
                }
            }

            int cc = 0;
            std::string res = "";
            for (char ch : result) {
                if (!((cc / b) % (2))) {
                    res += ch;
                }
                else {
                    res += " ";
                }
                cc++;
            }

            b++;
            vecResult->push_back(res);
        }
    }

    vecResult->push_back(encodedData);

    ret = (jobjectArray)env->NewObjectArray(5,env->FindClass("java/lang/String"),env->NewStringUTF(""));

    int ind = 0;
    for(std::string string : *vecResult){
        env->SetObjectArrayElement(ret,ind++,env->NewStringUTF(string.c_str()));
    }

    return(ret);
}
