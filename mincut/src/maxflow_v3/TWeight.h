class TWeight {

  public:

    int index;
    double w12;
    double w21;


    TWeight() {}
    TWeight(int id, double u, double v): index(id), w12(u), w21(v) {}

    TWeight(jdoubleArray& idTow, JNIEnv*& env) {

       jdouble weights[3];
       env->GetDoubleArrayRegion(idTow, 0, 3, weights);

       index = (int) weights[0];
       w12 = weights[1];
       w21 = weights[2];
    }
    
    TWeight(jobject& obj, JNIEnv*& env) {
      jclass jcClass = env->GetObjectClass(obj);
        
      jfieldID indexId = env->GetFieldID(jcClass, "index", "I"); 
      jfieldID w12Id = env->GetFieldID(jcClass, "w12", "D"); 
      jfieldID w21Id = env->GetFieldID(jcClass, "w21", "D"); 
      
      index = env->GetIntField(obj, indexId);
      w12 = env->GetDoubleField(obj, w12Id);
      w21 = env->GetDoubleField(obj, w21Id);
    }
};

std::vector<TWeight> read_tweights_edges(JNIEnv*& env, jobjectArray& inJNIArray) {
   jsize length = env->GetArrayLength(inJNIArray);
   std::vector<TWeight> res(length);

   for (int i = 0; i < length; i++) {
       jobject obj = (jobject) env->GetObjectArrayElement(inJNIArray, i);
       TWeight e(obj, env);
       res[i] = e;
   }
   return res;
}