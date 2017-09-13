export class ProductCategory {
    constructor(description:string, categoryId:string, imageUrl:string, headerUrl:string) {
        this.categoryId = categoryId;
        this.description = description;
        this.imageUrl = imageUrl;
        this.headerUrl = headerUrl;
    }

    categoryId:string;
    description:string;
    imageUrl:string;
    headerUrl:string;

    static getCategories() {
        return [
            new ProductCategory("Furniture Collections", "furniture-collections", "http://1.shard.www.biglots.com.edgesuite.net/i/w/v_oENGZBY.jpg", "http://images.biglots.com/073017-headerpagetitle-furniture-furniturecollections-bedroom?set=imageURL%5B%2Fimages%2Fmarketing%2F2017%2F073017-headerpagetitle-furniture-furniturecollections-bedroom.jpg%5D,env%5Bprod%5D,width%5B940%5D&call=url%5Bfile:biglots/marketing.chain%5D"),
            new ProductCategory("Living Room", "living-room", "http://1.shard.www.biglots.com.edgesuite.net/a/p/c6cVF8fso.jpg", "http://images.biglots.com/073017-headerpagetitle-furniture-furniturecollections-bedroom?set=imageURL%5B%2Fimages%2Fmarketing%2F2017%2F073017-headerpagetitle-furniture-furniturecollections-bedroom.jpg%5D,env%5Bprod%5D,width%5B940%5D&call=url%5Bfile:biglots/marketing.chain%5D"),
            new ProductCategory("Chairs & Recliners", "chairs-recliners", "http://1.shard.www.biglots.com.edgesuite.net/4/K/e9sB9zAVY.jpg", "http://images.biglots.com/073017-headerpagetitle-furniture-furniturecollections-bedroom?set=imageURL%5B%2Fimages%2Fmarketing%2F2017%2F073017-headerpagetitle-furniture-furniturecollections-bedroom.jpg%5D,env%5Bprod%5D,width%5B940%5D&call=url%5Bfile:biglots/marketing.chain%5D"),
            new ProductCategory("Fireplaces", "fireplaces", "http://1.shard.www.biglots.com.edgesuite.net/T/9/Ah5yDyAe0.jpg", "http://images.biglots.com/073017-headerpagetitle-furniture-furniturecollections-bedroom?set=imageURL%5B%2Fimages%2Fmarketing%2F2017%2F073017-headerpagetitle-furniture-furniturecollections-bedroom.jpg%5D,env%5Bprod%5D,width%5B940%5D&call=url%5Bfile:biglots/marketing.chain%5D")
        ];
    }
}



