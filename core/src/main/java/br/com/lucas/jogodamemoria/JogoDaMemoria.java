package br.com.lucas.jogodamemoria;

import java.util.ArrayList;
import java.util.Collections;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class JogoDaMemoria extends ApplicationAdapter {
    private SpriteBatch batch;
    Stage stage;
    BitmapFont textoFimJogo;
    ArrayList<Carta> cartasViradas;
    ArrayList<Carta> cartasIguais;
    float tempo = 0.5f;
    float contador = 0f;


    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new Stage();
		Gdx.input.setInputProcessor(stage);

        textoFimJogo = criarFonte(48, Color.WHITE);
        cartasViradas = new ArrayList<Carta>();
        cartasIguais = new ArrayList<Carta>();
        criarCartas(4);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        stage.draw();
        atualizarTempo();
        removerCartasIguais();
        compararCartas(cartasViradas);
        verificaFimJogo();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
    }

    private BitmapFont criarFonte(int tamanho, Color cor) {
		BitmapFont fonte;

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ARIALBD.TTF"));
		FreeTypeFontGenerator.FreeTypeFontParameter parametro = new FreeTypeFontGenerator.FreeTypeFontParameter();

		parametro.size = tamanho;
		parametro.color = cor;

		fonte = generator.generateFont(parametro);

		generator.dispose();

		return fonte;
	}

    private void criarCartas(int quantidade) {
        ArrayList<Integer> numeroCartas = new ArrayList<Integer>();
        for(int i = 1; i <= quantidade; i++) {
            numeroCartas.add(i);
            numeroCartas.add(i);
        }

        Collections.shuffle(numeroCartas);

        for(int i = 0, x = 50; i < quantidade; i++, x += 210) {
            Carta carta = new Carta(numeroCartas.get(i), x, Gdx.graphics.getHeight() - 300, this);
            stage.addActor(carta);
        }

        for(int i = quantidade, x = 50; i < quantidade * 2; i++, x += 210) {
            Carta carta = new Carta(numeroCartas.get(i), x, Gdx.graphics.getHeight() - 600, this);
            stage.addActor(carta);
        }
    }

    public void virarCartas() {
        for(Actor actor : stage.getActors()) {
            if(actor instanceof Carta)
            ((Carta)actor).virada = false;
        }
        cartasViradas.clear();
    }

    private void removerCartasIguais() {
        if(contador <= 0) {
            for(Carta carta : cartasIguais) {
                stage.getActors().removeValue(carta, true);
            }
            cartasIguais.clear();
        }
    }

    private void compararCartas(ArrayList<Carta> cartasViradas) {
        if(cartasViradas.size() == 2) {
            Carta carta1 = cartasViradas.get(0);
            Carta carta2 = cartasViradas.get(1);

            if(carta1.texturaFrente.toString().contentEquals(carta2.texturaFrente.toString())
            && carta1 != carta2) {
                cartasIguais.add(carta1);
                cartasIguais.add(carta2);
                contador = tempo;
                cartasViradas.clear();
            }
        }
    }

    private void atualizarTempo() {
        if(contador > 0) {
            contador -= Gdx.graphics.getDeltaTime();
        }
    }

    private void verificaFimJogo() {
        boolean jogoConcluido = true;
        for (Actor actor : stage.getActors()) {
            if(actor instanceof Carta) {
                jogoConcluido = false;
                break;
            }
        }

        if(jogoConcluido) {
            String mensagem = "Jogo finalizado!";

            batch.begin();

            GlyphLayout layout = new GlyphLayout(textoFimJogo, mensagem);
            float larguraTexto = layout.width;
            float alturaTexto = layout.height;

            float pos_x = (Gdx.graphics.getWidth() - larguraTexto) / 2;
            float pos_y = (Gdx.graphics.getHeight() + alturaTexto) / 2;

            textoFimJogo.draw(batch, mensagem, pos_x, pos_y);
            batch.end();
        }
    }

}
