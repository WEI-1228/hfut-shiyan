module counter2_tb;
reg cin,CLR,LD,CLK;
wire[3:0]out;
wire co;
counter2 uut(
.cin(cin),.CLR(CLR),.LD(LD),.CLK(CLK),.out(out),.co(co)
);

initial begin

CLK<=0;
cin<=1;
CLR<=1;
LD<=1;

#5 CLR=0;
#10 CLR=1;
#50 LD=0;
#10 LD=1;


end

always #10 CLK=~CLK;

endmodule;