package no.brinken.bambino;

import no.brinken.orm.IdField;
import no.brinken.orm.PrimaryKey;
import no.brinken.orm.TypeTableObject;

public class Event_type extends TypeTableObject
{
	@IdField
	@PrimaryKey
	public long event_type_id;
	public String event_name;
	public int measure_ind;
	
}
