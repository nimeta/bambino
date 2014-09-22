package no.brinken.bambino;

public class Measurement extends TableObject
{
	@PrimaryKey
	public long child_id;
	@PrimaryKey
	public String event_date;
	@PrimaryKey
	public long event_type_id;
	public int weight;
	public double length;
	public double circumferance;
}
