module register_TB;
reg[3:1]r1,r2,w1;
reg[16:1]wval;
reg CLK,WE;
wire[16:1]out1,out2;
register uut(
.r1(r1),.r2(r2),.w1(w1),.wval(wval),.CLK(CLK),.WE(WE),.out1(out1),.out2(out2)
);

initial begin
CLK<=0;
w1<=1;
WE<=1;
wval<=32;

#15;
w1<=2;
wval<=64;

#20;
w1<=3;
wval<=16;

#20;
WE=0;
r1=1;
r2=2;

#5
r1=2;
r2=3;

#10 $stop;
end

always #10 CLK<=~CLK;

endmodule