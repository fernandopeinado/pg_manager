package br.com.cas10.pgman.index;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class ExportQueue {

    private BlockingDeque<IndexedContent> queue = new LinkedBlockingDeque<>(1000);

    public void add(IndexedContent content) {
        if (!queue.offerLast(content)) {
            System.out.println("Despresando Conteudo " + content);
        }
    }

    public IndexedContent take() throws InterruptedException {
        return queue.takeFirst();
    }

}
