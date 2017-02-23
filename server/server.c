/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>

void error(const char *msg)
{
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[])
{
     int sockfd1,sockfd2, newsockfd1,newsockfd2, portno1,portno2;
     socklen_t clilen1,clilen2;
     char buffer[256];
     struct sockaddr_in serv_addr1, cli_addr1,serv_addr2, cli_addr2;
     int n,flag=0;
     if (argc < 2) {
         fprintf(stderr,"ERROR, no port provided\n");
         exit(1);
     }
     

       
      sockfd1 = socket(AF_INET, SOCK_STREAM, 0);
     if (sockfd1 < 0) 
        error("ERROR opening socket");
     bzero((char *) &serv_addr1, sizeof(serv_addr1));
     portno1 = atoi(argv[1]);
     serv_addr1.sin_family = AF_INET;
     serv_addr1.sin_addr.s_addr = INADDR_ANY;
     serv_addr1.sin_port = htons(portno1);
     if (bind(sockfd1, (struct sockaddr *) &serv_addr1,
              sizeof(serv_addr1)) < 0) 
              error("ERROR on binding");
      listen(sockfd1,5);
      clilen1 = sizeof(cli_addr1);
      newsockfd1 = accept(sockfd1, (struct sockaddr *) &cli_addr1,  &clilen1);
      if (newsockfd1 < 0) error("ERROR on accept");

      while(1)
      {
            
            //listen(sockfd1,5);
            //clilen1 = sizeof(cli_addr1);
            newsockfd2 = accept(sockfd1, (struct sockaddr *) &cli_addr1,  &clilen1);
            if (newsockfd1 < 0) error("ERROR on accept");
            flag =1;
            //printf("%d\n",newsockfd1);
          
            while(flag)
            {
                 bzero(buffer,256);
                 n = read(newsockfd2,buffer,255);
                 if (n < 0) error("ERROR reading from socket");
		 //buffer[3] = '\n';                 
                 if( buffer[0] == 'e' && buffer[1] == 'n' && buffer[2] == 'd')
                 {
                        printf("connet end\n");
                        close(newsockfd2);
                        flag = 0;
                        break;
                 }
                 if(n>2) printf("Here is the message: %s",buffer);
                 if(n>2) n = write(newsockfd1,buffer,255);
                 if (n < 0) error("ERROR writing to socket");
                 n=0;


            }
               
                 
      }
         
     close(newsockfd1);
     close(sockfd1);
     close(newsockfd2);
     close(sockfd2);
     return 0; 
}
