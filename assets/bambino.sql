CREATE TABLE "Child" (
"child_id" INTEGER NOT NULL,
"first_name" TEXT NOT NULL,
"last_name" TEXT,
"birth_date" TEXT NOT NULL,
"birth_time" TEXT,
"identity_no" TEXT,
PRIMARY KEY ("child_id") ,
CONSTRAINT "ak_Barn" UNIQUE ("first_name" ASC, "last_name" ASC)
);

CREATE TABLE "Measurement" (
"child_id" INTEGER NOT NULL,
"event_date" TEXT NOT NULL,
"event_type_id" INTEGER NOT NULL,
"weight" INTEGER NOT NULL,
"length" INTEGER NOT NULL,
"circumferance" INTEGER NOT NULL,
PRIMARY KEY ("event_date", "child_id", "event_type_id") ,
CONSTRAINT "fk_Measurement_Event" FOREIGN KEY ("child_id", "event_date", "event_type_id") REFERENCES "Event" ("child_id", "event_date", "event_type_id")
);

CREATE TABLE "Helsekontroll" (
"barn_id" INTEGER NOT NULL,
"dato" TEXT NOT NULL,
PRIMARY KEY ("dato", "barn_id") ,
CONSTRAINT "fk_Helsekontroll_Vaksine_1" FOREIGN KEY ("dato") REFERENCES "Vaccine" ("event_date")
);

CREATE TABLE "Appointment" (
"child_id" INTEGER NOT NULL,
"event_date" TEXT NOT NULL,
"event_type_id" INTEGER NOT NULL,
"appointment_time" TEXT NOT NULL,
"person_id" INTEGER,
PRIMARY KEY ("event_date", "child_id", "event_type_id") ,
CONSTRAINT "fk_Appointment_Person" FOREIGN KEY ("person_id") REFERENCES "Person" ("person_id"),
CONSTRAINT "fk_Appointment_Event" FOREIGN KEY ("child_id", "event_date", "event_type_id") REFERENCES "Event" ("child_id", "event_date", "event_type_id")
);

CREATE TABLE "Vaccine" (
"child_id" INTEGER NOT NULL,
"event_date" TEXT NOT NULL,
"event_type_id" INTEGER NOT NULL,
"vaccine_type_id" INTEGER NOT NULL,
PRIMARY KEY ("event_date", "vaccine_type_id", "child_id", "event_type_id") ,
CONSTRAINT "fk_Vaccine_Event" FOREIGN KEY ("child_id", "event_date", "event_type_id") REFERENCES "Event" ("child_id", "event_date", "event_type_id"),
CONSTRAINT "fk_Vaccine_Vaccine_type" FOREIGN KEY ("vaccine_type_id") REFERENCES "Vaccine_type" ("vaccine_type_id")
);

CREATE TABLE "Barselgruppemedlem" (
"medlem_id" INTEGER NOT NULL,
"fornavn" TEXT NOT NULL,
"etternavn" TEXT,
"telefonnr" TEXT,
"e-post" TEXT,
PRIMARY KEY ("medlem_id") 
);

CREATE TABLE "Event_type" (
"event_type_id" INTEGER NOT NULL,
"event_name" TEXT NOT NULL,
"measure_ind" INTEGER NOT NULL,
PRIMARY KEY ("event_type_id") 
);

CREATE TABLE "Event" (
"child_id" INTEGER NOT NULL,
"event_date" TEXT NOT NULL,
"event_type_id" INTEGER NOT NULL,
PRIMARY KEY ("child_id", "event_date", "event_type_id") ,
CONSTRAINT "fk_Event_Child" FOREIGN KEY ("child_id") REFERENCES "Child" ("child_id"),
CONSTRAINT "fk_Event_Event_type" FOREIGN KEY ("event_type_id") REFERENCES "Event_type" ("event_type_id")
);

CREATE TABLE "Vaccine_type" (
"vaccine_type_id" INTEGER NOT NULL,
"vaccine_name" TEXT NOT NULL,
PRIMARY KEY ("vaccine_type_id") 
);

CREATE TABLE "Clinic" (
"clinic_id" INTEGER NOT NULL,
"clinic_name" TEXT NOT NULL,
"address" TEXT,
"postal_no" TEXT,
"city" TEXT,
"phone" TEXT,
"e-mail" TEXT,
PRIMARY KEY ("clinic_id") 
);

CREATE TABLE "Person" (
"person_id" INTEGER NOT NULL,
"role_type_id" INTEGER NOT NULL,
"first_name" TEXT NOT NULL,
"last_name" TEXT,
"phone" TEXT,
"e-mail" TEXT,
PRIMARY KEY ("person_id") ,
CONSTRAINT "fk_Person_Role_type" FOREIGN KEY ("role_type_id") REFERENCES "Role_type" ("role_type_id")
);

CREATE TABLE "Role_type" (
"role_type_id" INTEGER NOT NULL,
"role_name" TEXT NOT NULL,
PRIMARY KEY ("role_type_id") 
);

CREATE TABLE "Clinic_person" (
"clinic_id" INTEGER NOT NULL,
"person_id" INTEGER NOT NULL,
PRIMARY KEY ("clinic_id", "person_id") ,
CONSTRAINT "fk_Clinic_person_Clinic" FOREIGN KEY ("clinic_id") REFERENCES "Clinic" ("clinic_id"),
CONSTRAINT "fk_Clinic_person_Person" FOREIGN KEY ("person_id") REFERENCES "Person" ("person_id")
);

