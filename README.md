# Akka Actor
- Akka makes it easier to write accurate concurrent, parallel, and distributed systems.
- Three parts : Actor , MailBox , Message
## Actor
-  an actor a trait
-  Once a class implements trait it becomes asynchronous
## messages
  - User can communicate with Actor using Messages
  - Messages can be object , string etc ., which is handled in receive
  - There are 2 types of messages - ask (?) , tell(!)
  - Ask : User expecting result in the for mof Future 
  - Tell : Fire and Forget
## mailbox:
  - is an internal data structure in Actor to store messages .
  - the user cannot access to it.
  - The actor gets message from  mailbox sequentially.

- Explain.scala

            print("1")
            actor ! "2"
            print("3")
            actor ! "4"
  - Output "1324" because Actor runs on the different thread 
  
