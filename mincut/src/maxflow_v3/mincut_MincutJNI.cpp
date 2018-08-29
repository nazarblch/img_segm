#include "mincut_MincutJNI.h"
#include "main.h"
#include <time.h>
#include <stdlib.h>
#include <vector>
#include <iostream>
#include "BidirEdge.h"
#include "TWeight.h"

jintArray toJArray (std::vector<int>& v, JNIEnv*& env) {
    
    jintArray jarr = env->NewIntArray(v.size());
    env->SetIntArrayRegion(jarr, 0, v.size(), &v[0]);

    return jarr;
}

void setMinCut(JNIEnv*& env, jobject& obj, double flow) {
    
  jclass jcClass = env->GetObjectClass(obj);
  jfieldID iId = env->GetFieldID(jcClass, "minCut", "D");
  
  env->SetDoubleField(obj, iId, flow);
}


JNIEXPORT jintArray JNICALL Java_mincut_MincutJNI_minCut
  (JNIEnv *env, jobject obj, jobjectArray edgeObjects, jobjectArray tweightObjects, jint nodesCount) {

    std::vector<BidirEdge> edges = read_bidir_edges(env, edgeObjects);
    std::vector<TWeight> tweights = read_tweights_edges(env, tweightObjects);
   
    Graph<double, double, double> *g = new Graph<double, double, double>(nodesCount, edges.size());
    
    g -> add_node(nodesCount);

    for (int i = 0; i < tweights.size(); i++) {
        g -> add_tweights(tweights[i].index, tweights[i].w12, tweights[i].w21);
    }

    for (int i = 0; i < edges.size(); i++) {
        g -> add_edge(edges[i].from, edges[i].to, edges[i].w12, edges[i].w21);
    }

    double flow = g -> maxflow();
    
    setMinCut(env, obj, flow);
    
    std::vector<int> fill = std::vector<int>(nodesCount);

    for (int i = 0; i < nodesCount; i++) {
       if (g->what_segment(i) == Graph<double, double, double>::SOURCE) {
          fill[i] = 0;
       } else {
          fill[i] = 1;
       }
    }

    delete g;

    return toJArray(fill, env);
}