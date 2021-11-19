package br.com.cas10.pgman.index;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class ExportQueue {

    @Value("${pgman.exportqueue.size:20000}")
    private int exportQueueSize;
    private BlockingDeque<IndexedContent> queue;

    @PostConstruct
    public void init() {
        this.queue = new LinkedBlockingDeque<>(exportQueueSize);
    }

    public void add(IndexedContent content) {
        if (!queue.offerLast(content)) {
            System.out.println("Despresando Conteudo " + content);
        }
    }

    public IndexedContent take() throws InterruptedException {
        return queue.takeFirst();
    }

}
