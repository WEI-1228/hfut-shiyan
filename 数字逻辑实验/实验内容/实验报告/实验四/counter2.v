module counter2(cin,CLR,LD,CLK,out,co);
	input cin,CLR,LD,CLK;
	output[3:0] out;
	output co;
	reg[3:0] out;
	
	always@(posedge CLK)
	begin
		if(!CLR)out<=4'b0000;
		else if(!LD)out<=4'b1111;
		else if(cin)
			if(out==4'b1111)out<=4'b0000;
			else out<=out+1'b1;
		else out<=out;
	
	end
	
	assign co=out[0]&out[1]&out[2]&out[3];
	
endmodule