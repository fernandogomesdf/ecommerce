import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Category {
  id: number;
  name: string;
}

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  available: boolean;
  category: Category;
}

export interface ProductResponse {
  content: Product[];
  page: {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
  };
}

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiBaseUrl = 'http://localhost:8080/api';
  private apiProductUrl = this.apiBaseUrl + '/products';
  
  private authHeader = 'Basic ' + btoa('admin:admin'); // should be kept safe

  constructor(private http: HttpClient) {}

  loadCategories(): Observable<Category[]> {
    const headers = new HttpHeaders().set('Authorization', this.authHeader);
    return this.http.get<Category[]>(this.apiBaseUrl + '/categories', { headers });
  }

  getProducts(page: number, size: number, search?: string): Observable<ProductResponse> {
    const headers = new HttpHeaders().set('Authorization', this.authHeader);
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search) {
      params = params.set('search', search);
    }

    return this.http.get<ProductResponse>(this.apiProductUrl, { headers, params });
  }

  createProduct(product: Product): Observable<Product> {
    const headers = new HttpHeaders().set('Authorization', this.authHeader);
    return this.http.post<Product>(this.apiProductUrl, product, { headers });
  }

  updateProduct(product: Product): Observable<Product> {
    const headers = new HttpHeaders().set('Authorization', this.authHeader);
    return this.http.put<Product>(`${this.apiProductUrl}/${product.id}`, product, { headers });
  }

  deleteProduct(id: number): Observable<void> {
    const headers = new HttpHeaders().set('Authorization', this.authHeader);
    return this.http.delete<void>(`${this.apiProductUrl}/${id}`, { headers });
  }
}
