`include "Demo.v"

module Demo_tb ();
	reg  clock;  
	reg  reset;
	reg  [1:0] io_in; 
	wire [1:0] io_out;
	wire       io_valid;

Demo dut(
	.clock (clock),
	.reset (reset),
	.io_in (io_in),
	.io_out(io_out),
	.io_valid(io_valid)
);

initial begin
    $dumpfile("wav.vcd");
    $dumpvars(0,io_out,io_in,io_valid);
	io_in = 1;
	#10;
end

endmodule
