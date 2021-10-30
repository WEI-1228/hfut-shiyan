module counter1_tb;
reg cin,CLR,LD,CLK;
wire[3:0]out;
wire co;
counter1 uut(
.cin(cin),.CLR(CLR),.LD(LD),.CLK(CLK),.out(out),.co(co)
);

initial begin

CLK<=0;
cin<=1;
LD<=1;
CLR<=1;
#4 LD<=0;
#1 LD<=1;
#100 CLR = 0;
#5 CLR = 1;
end

always #10 CLK<=~CLK;

endmodule