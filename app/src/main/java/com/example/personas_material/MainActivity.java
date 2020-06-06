package com.example.personas_material;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements AdaptadorPersona.OnPersonaClickListener {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		FloatingActionButton fab;
		RecyclerView lstPersonas;
		final ArrayList<Persona> personas;
		LinearLayoutManager llm;
		final AdaptadorPersona adapter;

		// Cambio 1
		DatabaseReference databaseReference;
		String db = "Personas";
		lstPersonas = findViewById(R.id.lstPersonas);
		//cambio 2
		personas = new ArrayList<>();
		llm = new LinearLayoutManager(this);
		adapter = new AdaptadorPersona(personas, this);
		llm.setOrientation(RecyclerView.VERTICAL);
		lstPersonas.setLayoutManager(llm);
		lstPersonas.setAdapter(adapter);
		fab = findViewById(R.id.btnAgregar);

		//Cambio 3
		databaseReference = FirebaseDatabase.getInstance().getReference();
		databaseReference.child(db).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				personas.clear();
				if(dataSnapshot.exists()){
					for(DataSnapshot snapshot: dataSnapshot.getChildren()){
						Persona p = snapshot.getValue(Persona.class);
						personas.add(p);
					}
				}
				adapter.notifyDataSetChanged();
				Datos.setPersonas(personas);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});


	}

	public void agregar(View v){
		Intent intent;
		intent = new Intent(MainActivity.this, AgregarPersona.class);
		startActivity(intent);
		//finish();
	}


	@Override
	public void onPersonaClick(Persona p) {
		Intent intent;
		Bundle bundle;

		bundle = new Bundle();
		bundle.putString("cedula", p.getCedula());
		bundle.putString("nombre", p.getNombre());
		bundle.putString("apellido", p.getApellido());
		bundle.putInt("foto", p.getFoto());

		intent = new Intent(MainActivity.this, DetallePersona.class);
		intent.putExtra("datos", bundle);
		startActivity(intent);
		//finish();
	}
}
