https://zebra-my.sharepoint.com/:f:/p/ms3267/EigLIz8t5uhIkK5i3GIh9PsBe8zP0sBlH-9Qe7AE3kD9_A?e=5%3a9dfUIr&at=

scp -oStrictHostKeyChecking=no -oUserKnownHostsFile=/dev/null symbol@10.17.131.112:/home/symbol/veeresh/embedded 30.3.2020/Zebra-FXSeries-Embedded-Native-SDK-C-CPP_Linux_V1.0.2/samples/gdbserver /tmp;/tmp/gdbserver  :2345 /apps/RFIDSample4App-C;exit

Last login: Mon Mar 30 08:00:23 2020 from 10.17.131.112

rfidadm@FX9600FCF3DE:~$ scp -oStrictHostKeyChecking=no -oUserKnownHostsFile=/dev 
/null symbol@10.17.131.112:/home/symbol/veeresh/embedded 30.3.2020/Zebra-FXSerie 
s-Embedded-Native-SDK-C-CPP_Linux_V1.0.2/samples/gdbserver /tmp;/tmp/gdbserver   
:2345 /apps/RFIDSample4App-C;exit
Warning: Permanently added '10.17.131.112' (ECDSA) to the list of known hosts.

symbol@10.17.131.112's password: Symbol@123

scp: /home/symbol/veeresh/embedded: No such file or directory
cp: can't stat '30.3.2020/Zebra-FXSeries-Embedded-Native-SDK-C-CPP_Linux_V1.0.2/samples/gdbserver': No such file or directory
Process /apps/RFIDSample4App-C created; pid = 11398
Listening on port 2345
Remote debugging from host 10.17.131.112
*** Error in `/apps/RFIDSample4App-C': malloc(): memory corruption: 0x0005d1e0 ***
ptrace(regsets_fetch_inferior_registers) PID=11503: No such process
ptrace(regsets_fetch_inferior_registers) PID=11503: No such process
logout




scp -oStrictHostKeyChecking=no -oUserKnownHostsFile=/dev/null symbol@10.17.131.112:/usr/share/Zebra-FXSeries-Embedded-Native-SDK-C-CPP_Linux_V1.0.2/samples/gdbserver /tmp;/tmp/gdbserver  :2345 /apps/RFIDSample4App-C;exit

rfidadm@FX9600FCF3DE:~$ scp -oStrictHostKeyChecking=no -oUserKnownHostsFile=/dev 
/null symbol@10.17.131.112:/usr/share/Zebra-FXSeries-Embedded-Native-SDK-C-CPP_L 
inux_V1.0.2/samples/gdbserver /tmp;/tmp/gdbserver  :2345 /apps/RFIDSample4App-C; 
exit
Warning: Permanently added '10.17.131.112' (ECDSA) to the list of known hosts.

symbol@10.17.131.112's password: Symbol@123


gdbserver                                       0%    0     0.0KB/s   --:-- ETA
gdbserver                                     100% 1319KB   2.8MB/s   00:00    
Process /apps/RFIDSample4App-C created; pid = 21624
Listening on port 2345
Remote debugging from host 10.17.131.112
*** Error in `/apps/RFIDSample4App-C': malloc(): memory corruption: 0x0005d1e0 ***
ptrace(regsets_fetch_inferior_registers) PID=21681: No such process
ptrace(regsets_fetch_inferior_registers) PID=21681: No such process


