module cpu_tb;

reg clk, rst;
cpu uut(
	.clk(clk), .rst(rst)
);
  
initial begin
clk = 1;
rst = 1;

#1 rst = 0;

#10 $stop;
end
  
  
  
always #1 clk = ~clk;
endmodule