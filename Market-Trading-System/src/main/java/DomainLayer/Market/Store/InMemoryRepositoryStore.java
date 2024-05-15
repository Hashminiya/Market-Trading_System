package DomainLayer.Market.Store;
import DomainLayer.Market.Store.Item;
import DomainLayer.Market.DataItem;
import DomainLayer.Market.InMemoryRepository;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.List;
import java.util.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.util.BytesRef;


public class InMemoryRepositoryStore extends InMemoryRepository<Long, Item> {
    private Directory index;
    private IndexWriter writer;
    public InMemoryRepositoryStore(){
        super();
        index = new RAMDirectory();
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try {
            writer = new IndexWriter(index, config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Item> search(String queryString) {
        List<Item> items = new ArrayList<>();
        try {
            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(index));
            QueryParser parser = new QueryParser("name", new StandardAnalyzer());
            Query query = parser.parse(queryString);
            TopDocs results = searcher.search(query, 10);
            for (ScoreDoc scoreDoc : results.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                Long itemId = Long.parseLong(doc.get("id"));
                Item item = this.data.get(itemId);
                if (item != null) {
                    items.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public void save(Item item) {
        indexItem(item);
        super.save(item);
        //TODO return an error if exist?
    }

    @Override
    public void update(Item item) {
        try {
            Term term = new Term("id", String.valueOf(item.getId()));
            writer.deleteDocuments(term);
            indexItem(item);
            super.update(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long key) {
        try {
            Term term = new Term("id", String.valueOf(key));
            writer.deleteDocuments(term);
            super.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void indexItem(Item item) {
        try {
            Document doc = new Document();
            doc.add(new StringField("id", String.valueOf(item.getId()), Field.Store.YES));
            doc.add(new TextField("name", item.getName(), Field.Store.YES));
            for (String category : item.getCategories()) {
                doc.add(new TextField("category", category, Field.Store.YES));
            }
            doc.add(new TextField("description", item.getDescription(), Field.Store.YES));
            doc.add(new DoublePoint("price", item.getPrice()));
            writer.addDocument(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<String> getAllCategoryValues() {
        List<String> categoryValues = new ArrayList<>();
        try {
            IndexReader reader = DirectoryReader.open(index);
            Terms terms = MultiFields.getTerms(reader, "category");
            TermsEnum iterator = terms.iterator();
            BytesRef byteRef;
            while ((byteRef = iterator.next()) != null) {
                String category = byteRef.utf8ToString();
                categoryValues.add(category);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categoryValues;
    }

}
