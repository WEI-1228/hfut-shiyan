module counter1(cin,CLR,LD,CLK,out,co);
	input cin,CLR,LD,CLK;
	output[3:0] out;
	output co;
	reg[3:0] out;
	
	always@(negedge CLR or posedge CLK or negedge LD)
	begin
		if(!LD)out<=4'b1111;
		else if(!CLR)out<=4'b0000;
		else if(cin)
		begin
			if(out==4'b1001)out<=4'b0000;
			else out=out+1'b1;
		end
		else out<=out;
	end
	
	assign co=out[3]&out[0];

endmodule