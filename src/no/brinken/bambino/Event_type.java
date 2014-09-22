package no.brinken.bambino;

public class Event_type extends TypeTableObject
{
	@IdField
	@PrimaryKey
	public long event_type_id;
	public String event_name;
	public int measure_ind;
	
}
