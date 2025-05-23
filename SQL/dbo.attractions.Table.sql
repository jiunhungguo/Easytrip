USE [easytrip]
GO
/****** Object:  Table [dbo].[attractions]    Script Date: 16/04/2025 21.20.22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[attractions](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[category] [nvarchar](255) NULL,
	[description] [nvarchar](max) NULL,
	[address] [nvarchar](255) NULL,
	[city_id] [int] NOT NULL,
	[latitude] [decimal](5, 2) NULL,
	[longitude] [decimal](5, 2) NULL,
	[rating] [decimal](2, 1) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
ALTER TABLE [dbo].[attractions]  WITH CHECK ADD  CONSTRAINT [FK_attractions_city] FOREIGN KEY([city_id])
REFERENCES [dbo].[cities] ([id])
GO
ALTER TABLE [dbo].[attractions] CHECK CONSTRAINT [FK_attractions_city]
GO
ALTER TABLE [dbo].[attractions]  WITH CHECK ADD CHECK  (([rating]>=(0) AND [rating]<=(5)))
GO
