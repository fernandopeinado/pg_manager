package br.com.cas10.pgman;

import br.com.cas10.pgman.index.IndexService;
import br.com.cas10.pgman.index.IndexedContent;
import br.com.cas10.pgman.index.ExportQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(Main.class, args);
        ExportQueue exportQueue = ctx.getBean(ExportQueue.class);
        IndexService indexService = ctx.getBean(IndexService.class);
        while (true) {
            IndexedContent indexedContent = exportQueue.take();
            indexService.save(indexedContent);
        }
    }
}
