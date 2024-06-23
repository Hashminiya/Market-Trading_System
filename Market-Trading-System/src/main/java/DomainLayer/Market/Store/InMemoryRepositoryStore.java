//package DomainLayer.Market.Store;
//
//import DomainLayer.Market.Util.InMemoryRepository;
//import org.apache.lucene.document.*;
//import org.apache.lucene.index.*;
//import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
//import org.apache.lucene.queryparser.classic.QueryParser;
//import org.apache.lucene.search.*;
//import org.apache.lucene.store.Directory;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.index.IndexWriter;
//import org.apache.lucene.store.RAMDirectory;
//import org.apache.lucene.util.BytesRef;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Repository;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.*;
//
//@Repository("InMemoryRepositoryStore")
//public class InMemoryRepositoryStore extends InMemoryRepository<Long, Item> {
//    private final int SEARCH_LIMIT = 30;
//    private final Directory index;
//    private final IndexWriter writer;
//    private final StandardAnalyzer analyzer;
//
//    public InMemoryRepositoryStore() {
//        super();
//        index = new RAMDirectory();
//        this.analyzer = new StandardAnalyzer();
//        IndexWriterConfig config = new IndexWriterConfig(analyzer);
//        try {
//            writer = new IndexWriter(index, config);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public List<Item> search(String category, String queryString, boolean withCategory) {
//        try {
//            IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(index));
//            Query finalQuery;
//
//            if (queryString == null || queryString.trim().isEmpty()) {
//                if (withCategory && category != null && !category.isEmpty()) {
//                    finalQuery = new TermQuery(new Term("category", category.toLowerCase()));
//                } else {
//                    // If no query string and no category, return empty results
//                    return new ArrayList<>();
//                }
//            } else {
//                QueryParser multiFieldParser = new MultiFieldQueryParser(new String[]{"name", "description"}, analyzer);
//                Query query = multiFieldParser.parse(queryString);
//                finalQuery = buildQuery(query, withCategory, category);
//            }
//
//            TopDocs results = searcher.search(finalQuery, SEARCH_LIMIT);
//            return asItemsList(results, searcher);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ArrayList<>();
//        }
//    }
//
//    private List<Item> asItemsList(TopDocs results, IndexSearcher searcher) throws IOException {
//        List<Item> items = new ArrayList<>();
//        for (ScoreDoc scoreDoc : results.scoreDocs) {
//            Document doc = searcher.doc(scoreDoc.doc);
//            Long itemId = Long.parseLong(doc.get("id"));
//            Item item = this.data.get(itemId);
//            if (item != null) {
//                items.add(item);
//            }
//        }
//        return items;
//    }
//
//    @Override
//    public void save(Item item) {
//        indexItem(item);
//        try {
//            writer.commit();
//            super.save(item);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void update(Item item) {
//        try {
//            Term term = new Term("id", String.valueOf(item.getId()));
//            writer.deleteDocuments(term);
//            indexItem(item);
//            writer.commit();
//            super.update(item);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public void delete(Long key) {
//        try {
//            Term term = new Term("id", String.valueOf(key));
//            writer.deleteDocuments(term);
//            writer.commit();
//            super.delete(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void indexItem(Item item) {
//        try {
//            Document doc = new Document();
//            doc.add(new StringField("id", String.valueOf(item.getId()), Field.Store.YES));
//            doc.add(new TextField("name", item.getName(), Field.Store.YES));
//            for (String category : item.getCategories()) {
//                doc.add(new StringField("category", category.toLowerCase(), Field.Store.YES));
//            }
//            doc.add(new TextField("description", item.getDescription(), Field.Store.YES));
//            doc.add(new DoublePoint("price", item.getPrice()));
//            writer.addDocument(doc);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<String> getAllCategoryValues() {
//        List<String> categoryValues = new ArrayList<>();
//        try {
//            IndexReader reader = DirectoryReader.open(index);
//            Terms terms = MultiFields.getTerms(reader, "category");
//            TermsEnum iterator = terms.iterator();
//            BytesRef byteRef;
//            while ((byteRef = iterator.next()) != null) {
//                String category = byteRef.utf8ToString();
//                categoryValues.add(category);
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return categoryValues;
//    }
//
//    private Query buildQuery(Query query, boolean withCategory, String category) {
//        BooleanQuery.Builder builder = new BooleanQuery.Builder();
//        builder.add(query, BooleanClause.Occur.MUST);
//        if (withCategory && category != null && !category.isEmpty()) {
//            builder.add(new TermQuery(new Term("category", category.toLowerCase())), BooleanClause.Occur.MUST);
//        }
//        return builder.build();
//    }
//}
