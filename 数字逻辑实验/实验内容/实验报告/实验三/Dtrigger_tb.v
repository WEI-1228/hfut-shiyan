module Dtrigger_tb;
reg D;
reg CLK;
reg set;
reg reset;
wire Q;
Dtrigger uut(
.D(D),.CLK(CLK),.set(set),.reset(reset),.Q(Q)
);
initial begin
CLK <= 1;
D<=1;
#40 D<=0;
#22 D<=1;
#40 D<=0;
#40 D<=1;
#10 D<=0;
#48 D<=1;
#24 D<=0;
#14 D<=1;
#20 reset<=0;
#10 reset<=1;
#30 D<=1;
#20 set<=0;
#20 set<=1;
#40 D<=0;
end
always
begin
#20 CLK<=~CLK;
end	
endmodule