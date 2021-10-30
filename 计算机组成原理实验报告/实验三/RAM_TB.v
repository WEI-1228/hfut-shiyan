module RAM_TB;
reg CLK,WE;
reg[9:1]addr;
reg[16:1]wval;
wire[16:1]wout;

RAM uut(
.CLK(CLK),.WE(WE),.addr(addr),.wval(wval),.wout(wout)
);

initial begin
CLK<=0;
WE<=0;
addr<=1;
wval<=16;

#15;
addr<=9'b111111111;
wval<=16'hffff;

#20;
addr<=2'b10;
wval<=17'b10000000000000000;

#20;
addr<=10'b1000000000;
wval<=4'hf;

#20 $stop;
end

always #10 CLK<=~CLK;

endmodule