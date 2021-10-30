module JKtrigger_tb;
reg J,K,clk,set,reset;
wire Q;
JKtrigger uut(
.J(J),.K(K),.clk(clk),.Q(Q),.set(set),.reset(reset)
);
initial begin
clk = 0;
K<=1;
J<=0;
#25 K<=0;
#7 J<=1;
#13 J<=0;
#10;
K<=1;
J<=1;
#30;
K<=0;
J<=0;
#5 reset<=0;
#15 reset<=1;
#5 set <=0;
end

always #10 clk=~clk;
endmodule