#include <iostream>
#include "main.h"

int main()
{

           Graph<int,int,int> *g = new Graph<int,int,int>(/*estimated # of nodes*/ 2, /*estimated # of edges*/ 1);

           g -> add_node();
           g -> add_node();

           g -> add_tweights( 0,   /* capacities */  1, 5 );
           g -> add_tweights( 1,   /* capacities */  2, 6 );
           g -> add_edge( 0, 1,    /* capacities */  3, 4 );

           int flow = g -> maxflow();

           printf("Flow = %d\n", flow);
           printf("Minimum cut:\n");
           if (g->what_segment(0) == Graph<int,int,int>::SOURCE)
                      printf("node0 is in the SOURCE set\n");
           else
                      printf("node0 is in the SINK set\n");
           if (g->what_segment(1) == Graph<int,int,int>::SOURCE)
                      printf("node1 is in the SOURCE set\n");
           else
                      printf("node1 is in the SINK set\n");

           delete g;

           return 0;
}
