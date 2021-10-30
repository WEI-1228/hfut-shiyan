module FourBitCmp(bigger,equal,smaller,ina,inb);
	parameter n=4;
	output bigger,equal,smaller;
	input[n:1] ina,inb;
	assign bigger=(ina>inb);
    assign equal=(ina==inb);
    assign  smaller=(ina<inb);
endmodule
