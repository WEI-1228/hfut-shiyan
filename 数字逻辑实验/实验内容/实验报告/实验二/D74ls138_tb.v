module D74ls138_tb;
	reg A,B,C;
	wire [7:0] out;
	wire [2:0] in;
	assign in={A,B,C};
	D74ls138 uut(.in(in),.out(out));
	initial begin
		A=0;B=0;C=0;#10;
		A=0;B=0;C=1;#10;
		A=0;B=1;C=0;#10;
		A=0;B=1;C=1;#10;
		A=1;B=0;C=0;#10;
		A=1;B=0;C=1;#10;
		A=1;B=1;C=0;#10;
		A=1;B=1;C=1;#10;
	end
endmodule