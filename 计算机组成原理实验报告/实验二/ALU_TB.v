module ALU_TB;

reg[16:1]A,B;
reg[3:1]op;
wire[16:1]out;
initial begin

A=12;
B=16;
op=3'b000;
#10;

A=32;
B=16;
op=3'b001;
#10;

A=15;
B=20;
op=3'b010;
#10;

op=3'b011;
#10;

A=2;
B=4;
op=3'b100;
#10;

A=64;
B=2;
op=3'b101;

#10 $stop;
end

ALU uut(
.A(A),.B(B),.op(op),.out(out)
);

endmodule