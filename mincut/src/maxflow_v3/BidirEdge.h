class BidirEdge {

  public:

    int from;
    int to;
    double w12;
    double w21;


    BidirEdge() {}
    BidirEdge(int id1, int id2, double u, double v): from(id1), to(id2), w12(u), w21(v) {}

    BidirEdge(jdoubleArray& idsTow, JNIEnv*& env) {

       jdouble weights[4];
       env->GetDoubleArrayRegion(idsTow, 0, 4, weights);
       from = (int)weights[0];
       to = (int)weights[1];
       w12 = weights[2];
       w21 = weights[3];
    }
    
    BidirEdge(jobject& obj, JNIEnv*& env) {
      jclass jcClass = env->GetObjectClass(obj);
      
      jfieldID fromId = env->GetFieldID(jcClass, "from", "I");    
      jfieldID toId = env->GetFieldID(jcClass, "to", "I"); 
      jfieldID w12Id = env->GetFieldID(jcClass, "w12", "D"); 
      jfieldID w21Id = env->GetFieldID(jcClass, "w21", "D"); 
      
      from = env->GetIntField(obj, fromId);
      to = env->GetIntField(obj, toId);
      w12 = env->GetDoubleField(obj, w12Id);
      w21 = env->GetDoubleField(obj, w21Id);
    }
    
};

std::vector<BidirEdge> read_bidir_edges(JNIEnv*& env, jobjectArray& inJNIArray) {
   jsize length = env->GetArrayLength(inJNIArray);
   std::vector<BidirEdge> res(length);

   for (int i = 0; i < length; i++) {
       jobject obj = (jobject)env->GetObjectArrayElement(inJNIArray, i);
       
       BidirEdge e(obj, env);
       res[i] = e;
   }
   return res;
}